package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.action.MerchantBusinessProductAction.SelectParams;
import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.ClientInterface;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("merchantBusinessProductService")
public class MerchantBusinessProductServiceImpl implements MerchantBusinessProductService {
	private static final Logger log = LoggerFactory.getLogger(MerchantBusinessProductServiceImpl.class);

	@Resource
	private MerchantBusinessProductDao merchantBusinessProductDao;

	@Resource
	private MerchantBusinessProductReadOnlyDao merchantBusinessProductReadOnlyDao;

	// 商户银行卡
	@Resource
	private MerchantCardInfoService merchantCardInfoService;

	// 审核记录
	@Resource
	private ExaminationsLogService examinationsLogService;

	// 商户信息
	@Resource
	private MerchantInfoService merchantInfoService;

	@Resource
	private AgentInfoService agentInfoService;

	// 商户服务限额信息
	@Resource
	private MerchantServiceQuotaService merchantServiceQuotaService;

	// 商户服务签约费率
	@Resource
	private MerchantServiceRateService merchantServiceRateService;

	// 商户进件条件表(明细)
	@Resource
	private MerchantRequireItemService merchantRequireItemService;

	@Resource
	private MerchantInfoDao merchantInfoDao;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private BusinessProductInfoDao businessProductInfoDao;

	@Resource
	private AuditorManagerService auditorManagerService;
	@Resource
	private SysDictDao sysDictDao;
	@Resource
	private SysConfigDao sysConfigDao;

	@Override
	public MerchantBusinessProduct selectByPrimaryKey(Long id) {
		return merchantBusinessProductDao.selectByPrimaryKey(id);
	}

	@Override
	public List<MerchantBusinessProduct> selectByParam(Page<MerchantBusinessProduct> page, SelectParams selectParams,
			String agentNo) {
		return merchantBusinessProductReadOnlyDao.selectByParam(page, selectParams, agentNo);
	}

	@Override
	public List<MerchantBusinessProduct> exportMerchantInfo(SelectParams selectParams, String loginAgentNo) {
		return merchantBusinessProductReadOnlyDao.exportMerchantInfo(selectParams, loginAgentNo);
	}

	@Override
	public int updateBymbpId(Long mbpId, String status, String auditorId) {
		return merchantBusinessProductDao.updateBymbpId(mbpId, status, auditorId);
	}

	@Override
	public int updateByItemAndMbpId(MerchantBusinessProduct record, MerchantInfo mis, List<MerchantRequireItem> mris,
			List<MerchantServiceRate> msr, List<MerchantServiceQuota> msq, int status) {
		int i = 0;

		int isApprove = (int) merchantInfoDao.isApprove(mis.getOneAgentNo());// 假设O为一级代理商
		if (isApprove == 1) {
			record.setStatus("1");// 待一审核
		}
		if (isApprove == 0) {
			record.setStatus("2");// 待平台审核
		}
		i = merchantBusinessProductDao.updateByItemAndMbpId(record);
		if (i != 1) {
			String msg = "失败:商户业务产品信息修改失败";
			throw new RuntimeException(msg);
		}

		MerchantInfo mm1 = merchantInfoService.selectMn(mis.getMerchantNo());
		if (mm1 == null) {
			throw new RuntimeException("商户信息修改失败");
		}
		MerchantInfo mm2 = merchantInfoService.selectMp(mis.getMobilephone(), mis.getTeamId());
		if (mm2 != null) {
			if (!mm2.getMerchantNo().equals(mis.getMerchantNo())) {
				throw new RuntimeException("手机号已存在，请重试");
			}
		}

		UserInfo uis = userInfoService.selectInfoByTelNo(mm1.getMobilephone(), mm1.getTeamId());
		if (uis == null) {
			throw new RuntimeException("手机号不存在，请重试");
		}
		int ppm = userInfoService.updateInfoByMp(mm1.getMobilephone(), mis.getMobilephone(), mis.getTeamId());
		if (ppm < 1) {
			throw new RuntimeException("手机号修改失败，请重试");
		}
		i = merchantInfoService.updateByMerId(mis);
		if (i != 1) {
			String msg = "失败:商户信息修改失败";
			throw new RuntimeException(msg);
		}
		for (MerchantRequireItem merchantRequireItem : mris) {
			if (merchantRequireItem.getExampleType().equals("1"))
				continue;
			MerchantRequireItem mrit = merchantRequireItemService.selectByMriId(merchantRequireItem.getMriId(),
					merchantRequireItem.getMerchantNo());
			if (!mrit.getContent().equals(merchantRequireItem.getContent())) {
				i = merchantRequireItemService.updateByMbpId(merchantRequireItem);
				if (i != 1) {
					String msg = "失败:商户进件项修改失败";
					throw new RuntimeException(msg);
				}
			}

		}

		for (MerchantServiceRate merchantServiceRate : msr) {
			if (merchantServiceRate.getFixedMark().equals("0")) {
				MerchantServiceRate m = new MerchantServiceRate();
				m = merchantServiceRateService.setMerchantServiceRate(merchantServiceRate);
				m.setId(merchantServiceRate.getId());
				m.setServiceId(merchantServiceRate.getServiceId());
				m.setMerchantNo(merchantServiceRate.getMerchantNo());
				m.setHolidaysMark(merchantServiceRate.getHolidaysMark());
				m.setCardType(merchantServiceRate.getCardType());
				m.setRateType(merchantServiceRate.getRateType());
				i = merchantServiceRateService.updateByPrimaryKey(m);
				if (i != 1) {
					String msg = "失败:商户费率改失败";
					throw new RuntimeException(msg);
				}
			}
		}

		for (MerchantServiceQuota merchantServiceQuota : msq) {
			if (merchantServiceQuota.getFixedMark().equals("0")) {
				i = merchantServiceQuotaService.updateByPrimaryKey(merchantServiceQuota);
				if (i != 1) {
					String msg = "失败:商户限额修改失败";
					throw new RuntimeException(msg);
				}
			}
		}

		if(i>0){
			// 修改商户手机号时调用积分系统修改手机号
			String vipScoreUrl=sysDictDao.SelectServiceId("VIP_SCORE_URL");
			String teamId=mis.getTeamId();
			String key=sysConfigDao.getStringValueByKey("VIP_SCORE_SIGN_KEY_"+teamId);
			String businessNo=sysConfigDao.getStringValueByKey("VIP_SCORE_BUS_NO_"+teamId);
			String resultMsg=ClientInterface.updateVipInfo(vipScoreUrl,mis,mm1.getMobilephone(),businessNo,teamId,key);
		}

		return i;
	}

	@Override
	public Object updateMerBusiOProInfo(int num, String merbpId, String auditorId) {
		int i = 0;
		if (num == 1) {
			// el.setOpenStatus("2");
			i += updateBymbpId(Long.valueOf(merbpId), "3", auditorId);
		} else {
			i += updateBymbpId(Long.valueOf(merbpId), "2", auditorId);
		}
		// el.setOpenStatus("1");
		// //商户表信息merchant_id
		// merchantInfoService.updateByPrimaryKey(merchantInfo);
		//
		// //商户银行卡信息merchant_id
		// mci.setDefSettleCard("1");
		// if(merchantCardInfoService.selectByMertId(merchantInfo.getMerchantNo())==null){
		// i+=merchantCardInfoService.insert(mci);
		// }else{
		// i+=merchantCardInfoService.updateById(mci);
		// }
		//
		// }
		// //审核记录表新增
		// i+=examinationsLogService.insert(el);

		return i;
	}

	@Override
	public List<MerchantBusinessProduct> selectAllByStatusInfo(Page<MerchantBusinessProduct> page, String agentNo) {
		return merchantBusinessProductReadOnlyDao.selectAllByStatusInfo(page, agentNo);
	}

	@Override
	public List<MerchantBusinessProduct> selectAllInfo(Page<MerchantBusinessProduct> page, String agentNo) {
		return merchantBusinessProductReadOnlyDao.selectAllInfo(page, agentNo);
	}

	@Override
	public List<String> querySerivceId(String bpId) {
		return businessProductInfoDao.findByProduct(bpId);
	}

	@Override
	public List<MerchantBusinessProduct> selectByStatusParam(Page<MerchantBusinessProduct> page,
			SelectParams selectParams, String agentNo) {
		return merchantBusinessProductReadOnlyDao.selectByStatusParam(page, selectParams, agentNo);
	}

	/**
	 * 更换业务产品流程
	 */
	@Override
	public boolean replaceBusinessProduct(final String merchantNo, String oldBpId, final String newBpId,
			String operationAgentNo) {
		log.info(String.format(
				"replaceBusinessProduct: merchantNo-> %s, oldBpId -> %s, newBpId -> %s, operationAgentNo->%s",
				merchantNo, oldBpId, newBpId, operationAgentNo));
		MerchantInfo merInfo = merchantInfoService.selectMn(merchantNo);
		if (merchantBusinessProductDao.updateMerBusTer(newBpId, merchantNo, oldBpId) < 1) {
			throw new AgentWebException("更新机具信息失败");
		}
		if (merchantBusinessProductDao.updateMerchantBusinessProduct(merchantNo, oldBpId, newBpId) != 1) {
			throw new RuntimeException("更新业务产品失败");
		}

		MerchantBusinessProductHistory mbpHis = new MerchantBusinessProductHistory();
		mbpHis.setSourceBpId(oldBpId);
		mbpHis.setNewBpId(newBpId);
		mbpHis.setOperationType("2");// 2更换
		mbpHis.setOperationPersonType("3");// 3代理商
		mbpHis.setCreateTime(new Date());
		mbpHis.setOperationPersonNo(operationAgentNo);// 操作代理商的编号
		mbpHis.setMerchantNo(merchantNo);
		if (merchantBusinessProductDao.insertMerBusProHis(mbpHis) != 1) {
			throw new AgentWebException("写入商户业务产品历史表失败");
		}
		// 删除审核失败进件项
		merchantBusinessProductDao.delectMerBusItem(merchantNo);
		updateMerchantService(merInfo, oldBpId, newBpId);
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		// 如果是再中付同步过,且同步成功的话,这需要条用中付的修改接口(修改费率)
		if (merchantBusinessProductDao.countZF_ZQAndSyncSuccess(merchantNo, newBpId) >= 1) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
						SysDict sysDict = sysDictDao.getByKey("ZFZQ_ACCESS_URL");
						String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerUpdate";
						Map<String, Object> marMap = new HashMap<String, Object>();

						marMap.put("merchantNo", merchantNo);
						marMap.put("bpId", newBpId);
						marMap.put("operator", principal.getUserId());
						marMap.put("changeSettleCard", "0");
						List<String> channelList = new ArrayList<>();
						channelList.add("ZF_ZQ");
						marMap.put("channelCode", channelList);
						String paramStr = JSON.toJSONString(marMap);
						log.info("ZFZQ_ACCESS_URL:" + accessUrl + "\n paramStr:" + paramStr);
						String result = new ClientInterface(accessUrl, null).postRequestBody(paramStr);
						log.error("调用上游同步返回数据:" + result);
					} catch (Exception e) {
						log.error("调用上游同步费率接口失败", e);
					}
				}
			}).start();
		}

		return true;

	}

	private void updateMerchantService(MerchantInfo merchantInfo, String oldBpId, String newBpId) {

		List<ServiceInfoBean> oldServiceInfoList = merchantBusinessProductDao.listServiceInfoByBpId(oldBpId);
		log.info("oldServiceInfoList -> " + oldServiceInfoList);
		List<ServiceInfoBean> newServiceInfoList = merchantBusinessProductDao.listServiceInfoByBpId(newBpId);
		log.info("newServiceInfoList -> " + newServiceInfoList);
		if (oldServiceInfoList == null || newServiceInfoList == null) {
			throw new AgentWebException("没有找到相应的服务信息");
		}
		if (oldServiceInfoList.size() != newServiceInfoList.size()) {
			throw new AgentWebException("新旧业务产品的服务信息不一一对应.");
		}
		Map<String, ServiceInfoBean> oldServiceInfoMap = translateServiceId2MapKey(oldServiceInfoList);
		Map<String, ServiceInfoBean> newServiceInfoMap = translateServiceType2MapKey(newServiceInfoList);
		log.info("oldServiceInfoMap -> " + oldServiceInfoMap);
		log.info("newServiceInfoMap -> " + newServiceInfoMap);

		String oneAgentNo = merchantInfo.getOneAgentNo();
		String merchantNo = merchantInfo.getMerchantNo();

		// 删除商户服务费率
		merchantBusinessProductDao.delectMerRate(oldBpId, merchantNo);
		// 删除商户服务限额
		merchantBusinessProductDao.delectMerQuota(oldBpId, merchantNo);

		for (Map.Entry<String, ServiceInfoBean> oldServiceEntry : oldServiceInfoMap.entrySet()) {
			String oldServiceId = oldServiceEntry.getKey();
			String oldServiceType = oldServiceEntry.getValue().getServiceType();
			ServiceInfoBean newServiceInfoBean = newServiceInfoMap.get(oldServiceType);
			if (newServiceInfoBean == null || StringUtils.isBlank(newServiceInfoBean.getServiceId())) {
				throw new AgentWebException("旧业务产品的的服务(" + oldServiceId + ")没找到对应的新服务");
			}
			String newServiceId = newServiceInfoBean.getServiceId();
			if (merchantBusinessProductDao.updateMerchantService(merchantNo, oldBpId, newBpId, oldServiceId,
					newServiceId) != 1) {
				throw new AgentWebException("更新商户服务失败");
			}
			if (StringUtils.equals("0", newServiceInfoBean.getFixedRate())) {
				List<ServiceRate> newServiceRateList = merchantInfoDao.getServiceRateByServiceId(oneAgentNo,
						newServiceId);
				if (newServiceRateList != null && !newServiceRateList.isEmpty()) {
					merchantBusinessProductDao.bacthInsertServiceRate(newServiceRateList, merchantNo);
				}
			}
			if (StringUtils.equals("0", newServiceInfoBean.getFixedQuota())) {
				List<ServiceQuota> newServiceQuotaList = merchantInfoDao.getServiceQuotaByServiceId(oneAgentNo,
						newServiceId);
				if (newServiceQuotaList != null && !newServiceQuotaList.isEmpty()) {
					merchantBusinessProductDao.bacthInsertServiceQuota(newServiceQuotaList, merchantNo);
				}
			}
		}
	}

	/**
	 * 把list转化为map,key 为 服务id(seriviceType)
	 * 
	 * @param serviceInfoList
	 * @return
	 */
	private Map<String, ServiceInfoBean> translateServiceType2MapKey(List<ServiceInfoBean> serviceInfoList) {
		Map<String, ServiceInfoBean> resultMap = new HashMap<>();
		for (ServiceInfoBean bean : serviceInfoList) {
			resultMap.put(bean.getServiceType(), bean);
		}
		return resultMap;
	}

	/**
	 * 把list转化为map,key 为 服务类型(serviceId)
	 * 
	 * @param serviceInfoList
	 * @return
	 */
	private Map<String, ServiceInfoBean> translateServiceId2MapKey(List<ServiceInfoBean> serviceInfoList) {
		Map<String, ServiceInfoBean> resultMap = new HashMap<>();
		for (ServiceInfoBean bean : serviceInfoList) {
			resultMap.put(bean.getServiceId(), bean);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> findFunctionManage(String functionNumber) {

		return merchantInfoDao.findFunctionManage(functionNumber);
	}

	@Override
	public Map<String, Object> findActivityIsSwitch(String oneAgentNo, String functionNumber) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
		return merchantInfoDao.findActivityIsSwitch(oneAgentNo, functionNumber, info.getTeamId());
	}

	@Override
	public List<BusinessProductDefine> selectGroupBpInfo(String agentNo, String bpId, String bpId1) {
		return merchantBusinessProductDao.selectGroupBpInfo(agentNo, bpId, bpId1);
	}

	@Override
	public Map<String, Object> selectMerBpInfo(String merchantNo, String bpId) {
		return merchantBusinessProductDao.selectMerBpInfo(merchantNo, bpId);
	}

	@Override
	public List<Map<String, Object>> selectTerBpInfo(String merchantNo, String bpId) {
		return merchantBusinessProductDao.selectTerBpInfo(merchantNo, bpId);
	}

	@Override
	public Integer selectMechant(SelectParams selectParams, String agentNo) {
		return merchantBusinessProductReadOnlyDao.selectMechant(selectParams, agentNo);
	}

	@Override
	public List<MerchantBusinessProduct> selectMerPro(String merchantNo) {
		return merchantBusinessProductDao.selectMerPro(merchantNo);
	}

	@Override
	public List<MerchantBusinessProduct> selectByParam(String merId) {
		return merchantBusinessProductDao.selectByParamByMerId(merId);
	}

	@Override
	public boolean isOpenAgentUpdateBpSwitch(String agentNo) {
		String oneAgentNo = agentInfoService.getOneAgentNo(agentNo);
		Map<String, Object> functionMap = findFunctionManage("003");
		if (functionMap == null || !"1".equals(Objects.toString(functionMap.get("function_switch")))) {
			return false;
		}
		// 看是否需要自审
		if ("1".equals(Objects.toString(functionMap.get("agent_control")))) {//代理商为开启
			Map<String, Object> autoManage = findActivityIsSwitch(oneAgentNo, "003");
			if (autoManage == null) {
				return false;
			}
		}
		return true;
	}
}
