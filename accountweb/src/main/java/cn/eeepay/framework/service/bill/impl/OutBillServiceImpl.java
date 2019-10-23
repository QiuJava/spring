package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.*;
import cn.eeepay.framework.dao.nposp.AcqOrgMapper;
import cn.eeepay.framework.dao.nposp.MerchantInfoMapper;
import cn.eeepay.framework.dao.nposp.OutAccountServiceMapper;
import cn.eeepay.framework.dao.nposp.OutAccountServiceRateMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.AccountStatus;
import cn.eeepay.framework.enums.OutBillRecordStatus;
import cn.eeepay.framework.model.bill.*;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.OutAccountService;
import cn.eeepay.framework.model.nposp.OutAccountServiceRate;
import cn.eeepay.framework.service.bill.*;
import cn.eeepay.framework.service.nposp.OutAccountServiceService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpConnectUtil;
import cn.eeepay.framework.util.JSONUtil;
import com.auth0.jwt.JWTSigner;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("outBillService")
@Transactional
public class OutBillServiceImpl implements OutBillService {

	private static final Logger log = LoggerFactory.getLogger(OutBillServiceImpl.class);
	@Resource
	private OutBillMapper outBillMapper;
	@Resource
	private OutBillDetailMapper outBillDetailMapper;
	@Resource
	private AcqOrgMapper acqOrgMapper;
	@Resource
	private MerchantInfoMapper merchantInfoMapper;
	@Resource
	private ExtAccountMapper extAccountMapper;
	@Resource
	private OutAccountServiceMapper outAccountServiceMapper;
	@Resource
	private OutAccountServiceRateMapper outAccountServiceRateMapper;
	@Resource
	private OutAccountServiceService outAccountServiceService;
	@Resource
	private AcqOutBillMapper acqOutBillMapper;
	@Resource
	public SubOutBillDetailMapper subOutBillDetailMapper;
	@Resource
	public GenericTableService genericTableService;
	@Resource
	public SubOutBillDetailLogsMapper subOutBillDetailLogsMapper;
	@Resource
	public DuiAccountDetailMapper duiAccountDetailMapper;
	@Resource
	public ExtAccountService extAccountService;
	@Resource
	private DuiAccountDetailService duiAccountDetailService;
	@Resource
	private OutBillDetailService outBillDetailService;

	@Value("${accountApi.http.url}")
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;

	@Value("${core2.http.url}")
	private String core2ApiHttpUrl;
	@Value("${core2.http.secret}")
	private String core2HttpSecret;

	@Override
	public int insertOutBill(OutBill outBill) throws Exception {
		return outBillMapper.insertOutBill(outBill);
	}

	@Override
	public int updateOutBillById(OutBill outBill) throws Exception {
		return outBillMapper.updateOutBillById(outBill);
	}

	@Override
	public int deleteOutBillById(Integer id) throws Exception {
		return outBillMapper.deleteOutBillById(id);
	}

	@Override
	public int deleteOutBillByTaskId(Integer taskId) throws Exception {
		return outBillMapper.deleteOutBillByTaskId(taskId);
	}

	@Override
	public OutBill findOutBillByTaskId(Integer taskId) {
		return outBillMapper.findOutBillByTaskId(taskId);
	}


	@Override
	public OutBill findOutBillTaskById(Integer id) {
		return outBillMapper.findOutBillTaskById(id);
	}
	@Override
	public List<OutBill> findOutBillList(String createTime1, String createTime2, Map<String, String> param, Sort sort,
			Page<OutBill> page) throws Exception {
		return outBillMapper.findOutBillList(createTime1, createTime2, param, sort, page);
	}

	@Override
	public OutBill findOutBillById(Integer id) {
		return outBillMapper.findOutBillById(id);
	}

	@Override
	public int updateOutBillStatus(Integer id, String uname) {
		// 更新出账详情的出账结果
		return outBillMapper.updateOutBillStatus(id, uname);
	}

	@Override
	public List<OutBill> findFailedOutBill() {
		return outBillMapper.findFailedOutBill();
	}

	@Override
	public int updateToClosedByTransTime(Date transTime) {
		return outBillMapper.updateToClosedByTransTime(transTime);
	}

	@Override
	public int updateExportFileName(Integer outBillId, String fileName, String uname) {
		return outBillMapper.updateExportFileName(outBillId, fileName, uname);
	}

	// 判断商户信息是否正确
	public String judgeMerchantInfo(List<OutBillDetail> list) {
		String wid = "OK";
		if (list == null || list.isEmpty()) {

		} else {
			String merchantNo = "";
			MerchantInfo mer = null;
			ExtAccount ext = null;
			for (OutBillDetail item : list) {
				merchantNo = item.getMerchantNo();
				mer = merchantInfoMapper.findMerchantInfoByUserId(merchantNo);
				if (mer == null) {
					wid = "商户" + merchantNo + "不存在"; // 商户不存在
					return wid;
				}
				ext = extAccountMapper.findExtAccountByMerchantNo(merchantNo);
				if (ext == null) {
					wid = "商户" + merchantNo + "不存在"; // 商户不存在
					return wid;
				} else if (ext.getSettlingAmount().compareTo(item.getOutAccountTaskAmount()) < 0) {
					wid = "商户" + merchantNo + "结算中金额不足"; // 商户结算中金额不足
					return wid;
				}
			}
		}
		return wid;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> confirmOut(OutBill outBill, UserInfo userInfo)
			throws Exception {

		Integer outBillId = outBill.getId();
		Integer outAccountBillMethod = outBill.getOutAccountBillMethod();
		String uname = userInfo.getUsername();
		Map<String, Object> msg = new HashMap<String, Object>();
		// 根据出账单id查询所有的出账单详情
		// 分组查出该出账单下所有的出款通道
		List<OutBillDetail> list = null;
//		List<OutAccountService> outAccountServiceList = null;
		OutAccountServiceRate outAccountServiceRate = null;
		Map<String, BigDecimal> serviceMoney = null;
		// AcqOrg acqOrg = null;
		int result = 0;
		try {
			//先判断是否合成 出账单明细
			List<OutBillDetail> dlist = outBillDetailService.findByOutBillId(outBill.getId());
			if(dlist.size() == 0 || dlist.isEmpty()){
				//先合成子出账单到出账单
				this.mergeSubOutBill(outBill.getId(), outBill.getAcqEnname());
			}
			//先update出账方式
//			outBill.setOutAccountBillMethod(outAccountBillMethod);
			this.updateOutBillById(outBill);
			
			List<OutBillDetail> acqList = outBillDetailMapper.findAcqOrgByOutBillId(outBillId);
			if (acqList != null && acqList.size() > 0) {
				String wid = "OK";
				for (OutBillDetail acq : acqList) {
					list = outBillDetailMapper.findPartByOutBillIdAndAcq(outBillId, acq.getAcqOrgNo());

					// 判断该list中所有出账单详情里面商户结算中金额是否足够
					/*
					 * wid = judgeMerchantInfo(list); if (!"OK".equals(wid)) {
					 * msg.put("status", false); msg.put("msg", wid); return
					 * msg; }
					 */

					// acqOrg =
					// acqOrgMapper.findAcqOrgByAcqEnname(acq.getAcqOrgNo());
					// List<Map<String, Object>> tlist = new
					// ArrayList<Map<String, Object>>();
					// Map<String, Object> map = null;
					MerchantInfo merInfo = null;
					// 查询出账任务
					OutAccountService outAccountService = outAccountServiceMapper.getById(outAccountBillMethod);

					BigDecimal outAmount = BigDecimal.ZERO; // 实际出账金额
					if (outAccountService == null) {
						msg.put("status", false);
						msg.put("msg", "收单机构" + acq.getAcqOrgNo() + "未查询到出款服务费信息");
						log.error(msg.toString());
						return msg;
					} else if (outAccountService.getServiceType() == 4) {// 线上单笔代付
						int sucNum = 0;
						int failNum = 0;
						JWTSigner signer = new JWTSigner(core2HttpSecret);
						ObjectMapper om = new ObjectMapper();
						// 线上单笔代付
						// 继续调用T1线上结算接口
						String t1online = core2ApiHttpUrl + "/settle/commitT1UpSettleMoney";
						Map<String, Object> t1onparams = null;
						// 循环出账单明细，调用
						for (OutBillDetail item : list) {
							if (item.getRecordStatus().equals(OutBillRecordStatus.SUCCESS.toString())) {
								outAmount = outAmount.add(item.getOutAccountTaskAmount());
								continue;
							}
							merInfo = merchantInfoMapper.findMerchantInfoByUserId(item.getMerchantNo());
							t1onparams = new HashMap<String, Object>();

							t1onparams.put("origOrderNo", item.getOrderReferenceNo());
							SimpleDateFormat dataTimeFormat = new SimpleDateFormat("yyyyMMdd");

							String orderDate = "";
							if(item.getTransTime()!=null){
								orderDate = dataTimeFormat.format(item.getTransTime());
							}
							t1onparams.put("origOrderDate", orderDate);

							t1onparams.put("settleUserNo", item.getMerchantNo());
							t1onparams.put("settleUserType", "M");
							t1onparams.put("settleType", "3");
							t1onparams.put("sourceSystem", "account");
							t1onparams.put("createUser", userInfo.getUsername());
							t1onparams.put("settleAmount", item.getOutAccountTaskAmount().toString());
							if (merInfo == null) {
								failNum++;
								continue;
							} else {
								t1onparams.put("agentNode", merInfo.getAgentNo());
							}
							if(StringUtils.isNotBlank(item.getAcqMerchantNo())){
								t1onparams.put("serServiceId", item.getAcqMerchantNo());//银联商户号
							}
							if(StringUtils.isNotBlank(item.getPlateMerchantEntryNo())){
								t1onparams.put("mbpId", item.getPlateMerchantEntryNo());//进件编号
							}
							t1onparams.put("holidaysMark", "0");
							t1onparams.put("acqenname", item.getAcqOrgNo());
							t1onparams.put("sourceOrderNo", item.getId().toString());
							t1onparams.put("sourceBatchNo", item.getOutBillId().toString());
							String token = signer.sign(t1onparams);
							log.info("T1线上接口调用url:" + t1online);
							String t1response = HttpConnectUtil.postHttp(t1online, "token", token);
							log.info("T1线上接口响应response:" + t1response);
							Boolean isMaybeJson = JSONUtil.mayBeJSON(t1response);
							if (!isMaybeJson) {
								msg.put("status", false);
								msg.put("msg", t1online+"连接不上");
								log.info(msg.toString());
								continue;
							}
							Map<String, Object> resp = om.readValue(t1response, Map.class);
							OutBillDetail outBillDetail = outBillDetailMapper.findOutBillDetailById(item.getId());
							// String outBillResult = "T1线上结算：出款失败";
							// Integer outBillStatus = 2;
							if (resp == null || "".equals(resp) || (boolean) resp.get("status") == false) {
								// 出款失败
								// outBillResult = "T1线上结算：出款失败";
								// outBillStatus = 2;
								outBillDetail.setOutBillResult("T1线上结算：出款失败");
								outBillDetail.setOutBillStatus(2);
								// outBillDetailMapper.updateOutBillResultById("T1线上结算：出款失败",2,
								// item.getId()); //出账失败
								failNum++;
							} else {
								outAmount = outAmount.add(item.getOutAccountTaskAmount());
								// 出款中
								// outBillResult = "T1线上结算：已提交出款系统";
								// outBillStatus = 3;
								outBillDetail.setOutBillResult("T1线上结算：已提交出款系统");
								outBillDetail.setOutBillStatus(3);
								// 更新商户的结算中金额
								// extAccountMapper.updateSubtractSettlingAmount(item.getOutAccountTaskAmount(),
								// item.getMerchantNo());
								// outBillDetailMapper.updateOutBillResultById("T1线上结算：已提交出款系统",1,
								// item.getId()); //出账失败
								sucNum++;
							}
							outBillDetail.setRecordStatus(OutBillRecordStatus.NORCORD.toString());// 未记账
							outBillDetailMapper.updateOutBillDetailById(outBillDetail);
							// 按交易出账时，实时检查状态，实时更新本地的出账状态
							List<SubOutBillDetail> subOutBillDetailList = new ArrayList<SubOutBillDetail>();
							subOutBillDetailList = subOutBillDetailMapper
									.querySubOutBillListByOutBillDetailId(outBillDetail.getId());
							for (SubOutBillDetail subOutBillDetail : subOutBillDetailList) {
								subOutBillDetail.setOutBillStatus(outBillDetail.getOutBillStatus());
								subOutBillDetail.setAcqEnname(item.getAcqOrgNo());
								subOutBillDetail.setSettleTime(new Date());
								if (subOutBillDetail.getOutBillStatus() == 2) {
									// 插入商户出账失败日志
									subOutBillDetail.setSubOutBillDetailId(subOutBillDetail.getId());
									subOutBillDetailLogsMapper.insertOutBillDetailLogs(subOutBillDetail);
									subOutBillDetail.setIsAddBill("0");
								} else {
									subOutBillDetail.setIsAddBill("1");
								}
								// 更新子出账单明细表
								subOutBillDetailMapper.updateOutBillStatusBySubOutBillDetailId(subOutBillDetail);
								// 更新对账详情表交易的出账状态
								duiAccountDetailMapper.updateOutBillStatusByOrderRefNum(subOutBillDetail,
										outAccountBillMethod);
							}
							// outBillDetailMapper.updateOutBillResultById(outBillResult,outBillStatus,
							// item.getId());
						}

						// 确认出账状态由否变为是
						if (failNum > 0) {
							msg.put("status", false);
							msg.put("msg", "T1线上结算：提交出款系统，成功" + sucNum + "条，失败" + failNum + "条");
							log.info(msg.toString());
						} else {
							result = outBillMapper.updateOutBillStatus(outBillId, uname);
							if (result > 0) {
								// 更新实际出账金额
								acqOutBillMapper.updateOutAmountByOutBillId(outAmount, outBillId);
								// 出账成功
								msg.put("status", true);
								msg.put("msg", "出账成功");
								log.info(msg.toString());
							} else {
								// 出账失败
								msg.put("status", false);
								msg.put("msg", "出账失败");
								log.error(msg.toString());
							}
						}
					} else if (outAccountService.getServiceType() == 5 || outAccountService.getServiceType() == 6) {// 线下批量代付   T1和Tn线下代付
                        String settleType = "4";
                        if(outAccountService.getServiceType() == 6){
                            settleType = "6";
                        }
						// 线下批量代付
						int sucNum = 0;
						int failNum = 0;
						outAccountServiceRate = outAccountServiceRateMapper
								.findServiceRateByServiceIdAndAgentRateTypeNotNull(outAccountService.getId());
						// boolean flag = false; //有记录已经单条出款，则不执行批量出款
						for (OutBillDetail item : list) {
							if (item.getOutBillStatus() == 3) {
								continue;
							}
							if (item.getRecordStatus().equals(OutBillRecordStatus.SUCCESS.toString())) {
								outAmount = outAmount.add(item.getOutAccountTaskAmount());
								continue;
							}
							try {
								// 继续调用T1线下结算接口
								JWTSigner signer2 = new JWTSigner(core2HttpSecret);
								ObjectMapper om2 = new ObjectMapper();
								String t1offline = core2ApiHttpUrl + "/settle/commitT1DownSettleMoney";
								Map<String, Object> t1offparams = null;
								merInfo = merchantInfoMapper.findMerchantInfoByUserId(item.getMerchantNo());
								t1offparams = new HashMap<String, Object>();
								serviceMoney = outAccountServiceService.acqOutServiceMoney(acq.getAcqOrgNo(),
										outAccountService.getId().toString(), item.getOutAccountTaskAmount(),
										outAccountServiceRate.getAgentRateType() == null ? null
												: outAccountServiceRate.getAgentRateType().toString(),
										outAccountServiceRate.getCostRateType() == null ? null
												: outAccountServiceRate.getCostRateType().toString());
								t1offparams.put("acqOutMoney2", serviceMoney.get("acqOutMoney2").toString());
								t1offparams.put("acqOutMoney1", serviceMoney.get("acqOutMoney1").toString());
								t1offparams.put("settleUserNo", item.getMerchantNo());
								t1offparams.put("settleUserType", "M");
								t1offparams.put("settleType", settleType);
								t1offparams.put("sourceSystem", "account");
								t1offparams.put("createUser", userInfo.getUsername());
								t1offparams.put("settleAmount", item.getOutAccountTaskAmount().toString());
								if (merInfo == null) {
									failNum++;
									continue;
								} else {
									t1offparams.put("agentNode", merInfo.getAgentNo());
								}
								t1offparams.put("acqenname", item.getAcqOrgNo());
								t1offparams.put("sourceOrderNo", item.getId().toString());
								t1offparams.put("sourceBatchNo", item.getOutBillId().toString());
								t1offparams.put("outBillStatus", "");
								t1offparams.put("holidaysMark", "0");

								String token2 = signer2.sign(t1offparams);
								log.info("服务类型："+outAccountService.getServiceType()+"/线下接口调用url:" + t1offline);
								String t1response = HttpConnectUtil.postHttp(t1offline, "token", token2);
								log.info("服务类型："+outAccountService.getServiceType()+"/线下接口调用response:" + t1response);
								Boolean isMaybeJson = JSONUtil.mayBeJSON(t1response);
								if (!isMaybeJson) {
									msg.put("status", false);
									msg.put("msg", t1offline+"连接不上");
									log.info(msg.toString());
									continue;
								}
								Map<String, Object> resp2 = om2.readValue(t1response, Map.class);
								OutBillDetail outBillDetail = outBillDetailMapper
										.findOutBillDetailById(item.getId());
								if (resp2 == null || "".equals(resp2) || (boolean) resp2.get("status") == false) {
									outBillDetail.setOutBillResult("服务类型："+outAccountService.getServiceType()+"/线下结算：执行失败");
									outBillDetail.setOutBillStatus(2);// 出账失败
									failNum++;
								} else {
									outBillDetail.setOutBillResult("服务类型："+outAccountService.getServiceType()+"/线下结算：出款中");
									outBillDetail.setOutBillStatus(3);// 出款中
									outAmount = outAmount.add(item.getOutAccountTaskAmount());
									// 更新商户的结算中金额
									// extAccountMapper.updateSubtractSettlingAmount(item.getOutAccountTaskAmount(),
									// item.getMerchantNo());
									sucNum++;
								}
								outBillDetail.setRecordStatus(OutBillRecordStatus.NORCORD.toString());// 未记账
								outBillDetailMapper.updateOutBillDetailById(outBillDetail);

								// 按交易出账时，实时检查状态，实时更新本地的出账状态
								List<SubOutBillDetail> subOutBillDetailList = new ArrayList<SubOutBillDetail>();
								subOutBillDetailList = subOutBillDetailMapper
										.querySubOutBillListByOutBillDetailId(outBillDetail.getId());
								for (SubOutBillDetail subOutBillDetail : subOutBillDetailList) {
									subOutBillDetail.setAcqEnname(item.getAcqOrgNo());
									subOutBillDetail.setOutBillStatus(outBillDetail.getOutBillStatus());
									subOutBillDetail.setSettleTime(new Date());
									if (subOutBillDetail.getOutBillStatus() == 2) {
										// 插入商户出账失败日志
										subOutBillDetail.setSubOutBillDetailId(subOutBillDetail.getId());
										subOutBillDetailLogsMapper.insertOutBillDetailLogs(subOutBillDetail);
										subOutBillDetail.setIsAddBill("0");
									} else {
										subOutBillDetail.setIsAddBill("1");
									}
									// 更新子出账单明细表
									subOutBillDetailMapper
											.updateOutBillStatusBySubOutBillDetailId(subOutBillDetail);
									// 更新对账详情表交易的出账状态
									duiAccountDetailMapper.updateOutBillStatusByOrderRefNum(subOutBillDetail,
											outAccountBillMethod);
								}

							} catch (Exception e) {

								msg.put("status", false);
								if (merInfo == null) {
									msg.put("msg", "系统未查询到商户:" + item.getMerchantNo());
								} else {
									msg.put("msg", "商户" + item.getMerchantNo() + "服务费率计算失败");
								}
								log.error("异常:", e);
								log.error(msg.toString());
								return msg;
							}
						}
						if (failNum > 0) {
							msg.put("status", false);
							msg.put("msg", "服务类型："+outAccountService.getServiceType()+"/线下结算：提交出款系统，成功" + sucNum + "条，失败" + failNum + "条");
							log.info(msg.toString());
						} else {
							outBill = outBillMapper.findOutBillById(outBillId);
							outBill.setOutBillStatus("1");
							result = outBillMapper.updateOutBillById(outBill);
							if (result > 0) {
								// 更新实际出账金额
								acqOutBillMapper.updateOutAmountByOutBillId(outAmount, outBillId);
								// 出账成功
								msg.put("status", true);
								msg.put("msg", "确认提交成功");
								log.info(msg.toString());
							} else {
								// 出账失败
								msg.put("status", false);
								msg.put("msg", "出账失败");
								log.info(msg.toString());
							}
						}
					} else {
						msg.put("status", false);
						msg.put("msg", "收单机构" + acq.getAcqOrgNo() + "出款服务费信息不正确");
						log.error(msg.toString());
						return msg;
					}
				}
			}
			return msg;
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "操作失败，系统异常");
			log.error("异常:", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void mergeSubOutBill(Integer outBillId, String acqEnname) throws Exception {
		List<SubOutBillDetail> list = null;

		//不需要合并子账单的收单机构
		List<String> acqList = new ArrayList<>();
		acqList.add("zm_pay");
		acqList.add("zm_xe");
		if(acqList.contains(acqEnname)){
			//不汇总
			list = subOutBillDetailMapper.findPartBySubOutBillIdAndAcqNotGroup(outBillId, acqEnname);
		}else{
			//直清通道 必改这里
			if("ZF_ZQ".equals(acqEnname) || "ZG_ZQ".equals(acqEnname)){
				list = subOutBillDetailMapper.findPartBySubOutBillIdAndAcqInZFZQ(outBillId, acqEnname);
			}else {
				list = subOutBillDetailMapper.findPartBySubOutBillIdAndAcq(outBillId, acqEnname);
			}
		}
		String outBillDetailId = "";
		int outBillNum = 0;
		for (SubOutBillDetail subOutBillDetail : list) {
			OutBillDetail outBillDetail = new OutBillDetail();
			outBillDetailId = genericTableService.outBillDetailId();
			outBillDetail.setId(outBillDetailId);
			outBillDetail.setOutBillId(outBillId);
			outBillDetail.setMerchantNo(subOutBillDetail.getMerchantNo());
			//直清通道 必改这里
			if("ZF_ZQ".equals(acqEnname) || "ZG_ZQ".equals(acqEnname)){
				outBillDetail.setPlateMerchantEntryNo(subOutBillDetail.getPlateMerchantEntryNo());
				outBillDetail.setAcqMerchantNo(subOutBillDetail.getAcqMerchantNo());
			}
			outBillDetail.setMerchantBalance(subOutBillDetail.getOutAccountTaskAmount());
			outBillDetail.setOutAccountTaskAmount(subOutBillDetail.getOutAccountTaskAmount());
			outBillDetail.setAcqOrgNo(subOutBillDetail.getAcqOrgNo());
			outBillDetail.setVerifyFlag(subOutBillDetail.getVerifyFlag());
			outBillDetail.setVerifyMsg(subOutBillDetail.getVerifyMsg());
			outBillDetail.setRecordStatus(OutBillRecordStatus.NORCORD.toString());
			outBillDetail.setExportStatus(0);
			outBillDetail.setOutBillStatus(0);// 未出账
			outBillDetail.setTransTime(subOutBillDetail.getTransTime());
			outBillDetail.setOrderReferenceNo(subOutBillDetail.getOrderReferenceNo());
			outBillNum = outBillDetailMapper.insertOutBillDetail(outBillDetail);
			if (outBillNum > 0) {
				SubOutBillDetail selectSubOutBillDetail = new SubOutBillDetail();
				selectSubOutBillDetail.setOutBillId(outBillId);
				selectSubOutBillDetail.setMerchantNo(subOutBillDetail.getMerchantNo());
				selectSubOutBillDetail.setPlateMerchantEntryNo(subOutBillDetail.getPlateMerchantEntryNo());
				List<SubOutBillDetail> queryList = new ArrayList<>();
				if("YS_ZQ".equals(acqEnname)){
					queryList = subOutBillDetailMapper.querySubIdByOutBillIdAndMerchantNoAndEntryNo(selectSubOutBillDetail);
				}else{
					queryList = subOutBillDetailMapper.querySubIdByOutBillIdAndMerchantNo(selectSubOutBillDetail);
				}
				for (SubOutBillDetail querySubOutBillDetail : queryList) {
					querySubOutBillDetail.setOutBillDetailId(outBillDetailId);
					subOutBillDetailMapper.updateSubIdByOutBillIdAndMerchantNo(querySubOutBillDetail);
				}
			}
		}
	}

	@Override
	public Map<String, Object> judgeConfirmOut(Integer outBillId, String acqEnname) {
		Map<String, Object> resultMapData = new HashMap<String, Object>();
		List<SubOutBillDetail> list = null;
		String currencyNo = "";
		String accountType = "M";
		String accountOwner = ""; // 机构组织id
		String merchantNo = "";
		list = subOutBillDetailMapper.findAllsubOutBill(outBillId, acqEnname);
		try {
			for (SubOutBillDetail subOutBillDetail : list) {
				merchantNo = subOutBillDetail.getMerchantNo();
				ExtAccountInfo extAccountInfo;

				extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, merchantNo, accountOwner, "",
						"224101001", currencyNo);
				if (extAccountInfo == null) {
					resultMapData.put(subOutBillDetail.getId(), "账户" + merchantNo + "不存在");
				} else {
					ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
					if (AccountStatus.DESTROY.toString().equals(extAccount.getAccountStatus())) {
						resultMapData.put(subOutBillDetail.getId(), "账户" + merchantNo + "销户");
					} else if (AccountStatus.FREEZE_ONLY_IN_DENY_OUT.toString().equals(extAccount.getAccountStatus())) {
						resultMapData.put(subOutBillDetail.getId(), "账户" + merchantNo + "冻结只进不出");
					} else if (AccountStatus.FREEZE_DENY_IN_DENY_OUT.toString().equals(extAccount.getAccountStatus())) {
						resultMapData.put(subOutBillDetail.getId(), "账户" + merchantNo + "冻结不进不出");
					}
				}

				DuiAccountDetail returnDuiAccountDetail = new DuiAccountDetail();
				returnDuiAccountDetail = duiAccountDetailService.queryDuiAccountDetailByOrderRefNum(subOutBillDetail);
				if (returnDuiAccountDetail != null) {
					if (StringUtils.isNotBlank(returnDuiAccountDetail.getFreezeStatus())) {
						if (!"0".equals(returnDuiAccountDetail.getFreezeStatus())) {
							resultMapData.put(subOutBillDetail.getId(), "已经被冻结");
						}
					}
				}

			}
		} catch (Exception e) {
			log.error("异常:", e);
		}
		return resultMapData;
	}

    @Override
    public OutBill findCreateTime(String acqEnname, String createTime) {
        return outBillMapper.findCreateTime(acqEnname,createTime);
    }

    @Override
    public OutAccountTask findTransTime(String acqName, String tTime) {
        return outBillMapper.findTransTime(acqName, tTime);
    }

    @Override
	public List<OutBill> findAllNoOutBillId() {
		return outBillMapper.findAllNoOutBillId();
	}

	@Override
	public BigDecimal findCalAmountByAcqNameAndOutBillStatus(String acqEnname) {
		return outBillMapper.findCalAmountByAcqNameAndOutBillStatus(acqEnname);
	}

}
