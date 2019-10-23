package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.AccountRecordService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.MerchantPreFreezeLogDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AccountCollectiveTransOrder;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.ScanCodeTrans;
import cn.eeepay.framework.model.SettleTransfer;
import cn.eeepay.framework.model.ShareSettleInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.TransInfo;
import cn.eeepay.framework.model.TransInfoFreezeNewLog;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.TransInfoFreezeNewLogService;
import cn.eeepay.framework.service.TransInfoService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;

@Service("transInfoService")
@Transactional
public class TransInfoServiceImpl implements TransInfoService{
	
	private static final Logger log = LoggerFactory.getLogger(TransInfoServiceImpl.class);
	@Resource
	private TransInfoDao transInfoDao;
	
	@Resource
	private AgentInfoService agentInfoService;
	
	@Resource
	private MerchantInfoDao merchantInfoDao;

	@Resource
	public TransInfoFreezeNewLogService transInfoFreezeNewLogService;
	
	@Resource
	private SysDictDao sysDictDao;
	@Resource
	private AccountRecordService accountRecordService;

	@Override
	public List<CollectiveTransOrder> queryAllInfo(CollectiveTransOrder transInfo, Page<CollectiveTransOrder> page) {
		transInfo.setPageSize(page.getPageSize());
		transInfo.setPageFirst(page.getFirst());
		List<CollectiveTransOrder> list=transInfoDao.queryAllInfo(transInfo);
		page.setResult(list);
		return list;
	}

	@Override
	public CollectiveTransOrder queryNumAndMoney(CollectiveTransOrder transInfo) {
		SysDict superPushBp = sysDictDao.getByKey("SUPER_PUSH_BP_ID");
		if(superPushBp!=null){
			transInfo.setSuperPushBpId(superPushBp.getSysValue());
		}
		return transInfoDao.queryNumAndMoney(transInfo);
	}

	@Override
	public CollectiveTransOrder queryInfoDetail(String id) {
		return transInfoDao.queryInfoDetail(id);
	}

	@Override
	public CollectiveTransOrder queryCollectiveTransOrder(String id) {
		return transInfoDao.queryCollectiveTransOrder(id);
	}

	@Override
	public int updateInfoByOrderNo(CollectiveTransOrder tt) {
		return transInfoDao.updateInfoByOrderNo(tt);
	}

	@Override
	public TransInfo queryInfo(String orderNo) {
		return transInfoDao.queryInfo(orderNo);
	}

	@Override
	public CollectiveTransOrder queryCtoInfo(String orderNo) {
		return transInfoDao.queryCtoInfo(orderNo);
	}

	@Override
	public CollectiveTransOrder selectByOrderNo(int agentNode, String orderNo) {
		return transInfoDao.selectByOrderNo(agentNode, orderNo);
	}

	@Override
	public List<SettleTransfer> selectSettleInfo(String tranId, String orderNo) {
		return transInfoDao.selectSettleInfo(tranId, orderNo);
	}

	@Override
	public List<CollectiveTransOrder> queryAllInfoSale(String str, CollectiveTransOrder transInfo,
			Page<CollectiveTransOrder> page) {
		List<String> list=agentInfoService.selectAllNodeSale(str);
		if(list.size()==0){
			return null;
		}
		//xy328tgh
//		String str1="";
//		for (String agentInfo : list) {
//			List<AgentInfo> list2 = agentInfoService.selectAllInfoSale1(agentInfo);
//			for (AgentInfo agentInfo2 : list2) {
//				str1+=" or tis.agent_no="+agentInfo2.getAgentNo();
//			}
//		}
//		if(str1.equals("")){
//			return null;
//		}
		transInfo.setSaleName(str);
		List<CollectiveTransOrder> clist=transInfoDao.queryAllInfoSale(transInfo, page);
		String money=transInfoDao.queryMoneySale(transInfo);
		if(money==null){
			money="0";
		}
		if(clist.size()==0){
			return null;
		}
		clist.get(0).setTotalMoney(money);
		return clist;
	}

	@Override
	public AccountCollectiveTransOrder queryInfoAccount(String orderNo) {
		return transInfoDao.queryInfoAccount(orderNo);
	}

	@Override
	public List<CollectiveTransOrder> selectAllRecordAccountFail() {
		return transInfoDao.selectAllRecordAccountFail();
	}

	@Override
	public int updateAccount(CollectiveTransOrder info) {
		return transInfoDao.updateAccount(info);
	}

	@Override
	public ScanCodeTrans getScanCodeTransByOrder(String orderNo) {
		return transInfoDao.getScanCodeTransByOrder(orderNo);
	}

	@Override
	public String findCardById(int id) {
		return transInfoDao.findCardById(id);
	}

	@Override
	public List<CollectiveTransOrder> importAllInfo(CollectiveTransOrder transInfo) {
		return transInfoDao.importAllInfo(transInfo);
	}

	@Override
	public ScanCodeTrans queryScanInfo(String orderNo) {
		return transInfoDao.queryScanInfo(orderNo);
	}

	/**
	 * 2.2.5添加预冻结金额判断
	 */
	@Override
	public Map<String, Object> judgePreFreezeaMountAngFreezaTrans(CollectiveTransOrder info) {
		
		log.info("调用账户的接口，定时任务开始,判断该笔交易是否冻结，交易单号是 orderNo ： " + info.getOrderNo());
		Map<String, Object> returnMap = new HashMap<String, Object>();
		MerchantInfo merchantInfo = null;
		Map<String, Object> map = new HashMap<String, Object>();
		synchronized(this){
		  try {
			// 获得该商户的预冻结金额				
			BigDecimal originalPreFrozenMoney = BigDecimal.ZERO;
			merchantInfo = merchantInfoDao.selectByMerchantNo(info.getMerchantNo());
			if(!StringUtils.isNotBlank(info.getMerchantNo())){
				returnMap.put("msg", "商户号为空！");
				return returnMap;
			}
			originalPreFrozenMoney = originalPreFrozenMoney.add(merchantInfo.getPreFrozenAmount());
			//如果商户的预冻结金额大于零，则冻结该笔交易
			if(originalPreFrozenMoney.compareTo(BigDecimal.ZERO) > 0 ){
				log.info("商户的预冻结金额为:"+ originalPreFrozenMoney +"，进入冻结接口，交易单号是 orderNo ： " + info.getOrderNo());
				//插入冻结日志
				TransInfoFreezeNewLog tfnl=new TransInfoFreezeNewLog();
				tfnl.setOperId("system");
				tfnl.setOperName("system");
				String fd="1";
				tfnl.setOperReason("触发预冻结,且冻结"+info.getTransAmount()+"元");
				tfnl.setOperType("0");
				tfnl.setOrderNo(info.getOrderNo());
				tfnl.setOperTime(new Date());
				//调 冻结接口
				map = transInfoFreezeNewLogService.insertInfo(tfnl,info,fd,"000017","system", "system", originalPreFrozenMoney);
				log.info("调用冻结接口返回结果为: " + map.toString());
				returnMap.put("msg", map.toString());
			}else{
				returnMap.put("msg", "预冻结金额为0，不需要冻结");
			}
		} catch (Exception e) {
			log.error("boss预冻结交易异常" + e);
			returnMap.put("msg", "预冻结异常"+e);
		}
	  }
		return returnMap;
	}
	@Override
	public int updateFreezeStatusByOrderNo(String orderNo, String freezeStatus) {
		return transInfoDao.updateFreezeStatusByOrderNo(orderNo, freezeStatus);
	}

	@Override
	public TransInfo findTransInfoByAcqReferenceNo(String acqReferenceNo) {
		return transInfoDao.findTransInfoByAcqReferenceNo(acqReferenceNo);
	}

	@Override
	public Map<String, Object> getSettle(List<String> orderNoArr) {
		//JSONObject jsons=null;
		//String result = "";
		//Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> map1=new HashMap<String, Object>();
		//List<CollectiveTransOrder> collectiveTransOrders = transInfoDao.getSettle(orderNoArr);
		//final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//int failNum = 0;
		//for (CollectiveTransOrder collectiveTransOrder : collectiveTransOrders) {
		//	Integer id = collectiveTransOrder.getId();
		//	String url = Constants.SETTLE_TRANS + "?transferId=" + id.toString()+"&userId="+principal.getId().toString();
		//	result = ClientInterface.baseNoClient(url, map);
		//	jsons = JSON.parseObject(result);
//			if (!jsons.getBoolean("status")) {
//				map1.put("bols", false);
//				map1.put("msg", jsons.get("msg").toString());
//			} else {
//				map1.put("bols", true);
//				map1.put("msg", jsons.get("msg").toString());
//			}
		//	if (!jsons.getBoolean("status")) {
		//		failNum++;
		//		log.error("批量结算失败,订单号为{},调用账户系统返回结果[{}]",collectiveTransOrder.getOrderNo(),jsons.getString("msg"));
		//	}
		//}
		//map1.put("bols", true);
		//map1.put("failNum", failNum);
		return map1;
	}

	/**
	 * 手动结算
	 * 先做数据验证，验证成功后，再调用结算接口
	 */
	@Override
	public Map<String, Object> settleTransInfo(String id) {
		Map<String, Object> msg = new HashMap<>();
		Map<String, String> map=new HashMap<String, String>();
		msg.put("bols", false);
		msg.put("msg", "结算失败");
		CollectiveTransOrder cto = transInfoDao.queryCtoForSettle(id);
		/**
		 *  T0,
		 *  当天(去掉)
		 *  交易成功
		 *  交易记账:记账成功
	     *  冻结状态:正常
		 *  结算状态:结算失败\未结算
		 *  没有T1结算过
		 *  陈达娟提供可结算的条件
		 */
		if(cto!=null && cto.getTransTime()!=null&&"0".equals(cto.getSettlementMethod())
				&&"SUCCESS".equals(cto.getTransStatus())
				&&"1".equals(cto.getAccount())
				&&"0".equals(cto.getFreezeStatus())
				&& cto.getSettleType()==null&&cto.getSettleOrder()==null
				&&("0".equals(cto.getSettleStatus())||"3".equals(cto.getSettleStatus()))){
			//满足结算条件
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String url=Constants.SETTLE_TRANS+"?transferId="+cto.getId()+"&userId="+principal.getId().toString();
			String result=ClientInterface.baseNoClient(url,map);
			log.info("手工结算，出款url：" + url);
			log.info("手工结算，出款返回信息：" + result);
			JSONObject jsons=JSON.parseObject(result);
			if((Boolean)jsons.get("status")){
				msg.put("bols", true);
				msg.put("msg", "结算成功");
			}else{
				msg.put("bols", false);
				msg.put("msg", "结算失败");
			}
			msg.put("transAmount", cto.getTransAmount().toString());
		}else{
			msg.put("bols", false);
			msg.put("msg", "不符合结算条件");
		}
		return msg;
	}

	@Override
	public Map<String, Object> settleButch(List<CollectiveTransOrder> collectiveTransOrders) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("bols", false);
		msg.put("msg", "结算失败");
		if(collectiveTransOrders!=null){
			int successTimes = 0;//成功次数
			int failTimes = 0;//失败次数
			BigDecimal successAmount = new BigDecimal("0.00");//成功总金额
			BigDecimal failAmount = new BigDecimal("0.00");//失败总金额
			for(CollectiveTransOrder cto: collectiveTransOrders){
				try {
					Map<String, Object> resultMap = settleTransInfo(cto.getOrderNo());
					//结算成功
					if((boolean)resultMap.get("bols")){
						successTimes++;
						successAmount = successAmount.add(cto.getTransAmount());
					} else {
						failTimes++;
						failAmount = failAmount.add(cto.getTransAmount());
					}
				} catch (Exception e) {
					failTimes++;
					failAmount = failAmount.add(cto.getTransAmount());
					log.error("手工结算失败,订单号为：" + cto.getOrderNo());
					log.error("手工结算失败",e);
				}
			}
			msg.put("bols", true);
			msg.put("msg", "结算成功");
			msg.put("successTimes", successTimes);
			msg.put("successAmount", successAmount.toString());
			msg.put("failTimes", failTimes);
			msg.put("failAmount", failAmount.toString());
		}
		return msg;
		
	}

	@Override
	public List<ShareSettleInfo> queryShareSettleInfo(ShareSettleInfo ssInfo, Page<ShareSettleInfo> page) {
		return transInfoDao.queryShareSettleInfo(ssInfo,page);
	}

	@Override
	public int insertShareSettleInfo(ShareSettleInfo ssInfo) {
		return transInfoDao.insertShareSettleInfo(ssInfo);
	}

	@Override
	public List<ShareSettleInfo> exportShareSettleInfo(ShareSettleInfo ssInfo) {
		return transInfoDao.exportShareSettleInfo(ssInfo);
	}
//	@Override
//	public List<CollectiveTransOrder> getUnSettle(String channelNames, Date startDate, Date endDate, Integer limitNumbers){
//		return transInfoDao.getUnSettele(channelNames,startDate,endDate,limitNumbers);
//	}

	@Override
	public Map<String, Object> syncOrder(List<CollectiveTransOrder> collectiveTransOrders) {
		Map<String, Object> msg = new HashMap<>();
		msg.put("bols", false);
		msg.put("msg", "同步失败");
		if(collectiveTransOrders!=null){
			int successTimes = 0;//成功次数
			int failTimes = 0;//失败次数
			for(CollectiveTransOrder cto: collectiveTransOrders){
				try {
					int i = transInfoDao.syncOrder(cto);
					//结算成功
					if(i == 1){
						successTimes++;
					} else {
						failTimes++;
					}
				} catch (Exception e) {
					failTimes++;
					log.error("手工同步失败,订单号为：" + cto.getOrderNo());
					log.error("手工同步失败",e);
				}
			}
			msg.put("bols", true);
			msg.put("msg", "同步成功");
			msg.put("successTimes", successTimes);
			msg.put("failTimes", failTimes);
		}
		return msg;
	}

	@Override
	public Map<String, Object> getAmountAndNum(String merchantNo, String startTime, String endTime) {
		return transInfoDao.getAmountAndNum(merchantNo,startTime,endTime);
	}

	/**
	 * 同步交易状态
	 */
	@Override
	public Map<String, Object> syncTransStatus() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询 当天交易状态为"已提交"的订单
			List<String> orderNos = transInfoDao.syncTransStatus();
			log.info("本次同步交易状态数据条数：" + orderNos.size());
			String returnMsg = ClientInterface.syncTransStatus(JSON.toJSONString(orderNos));
			map.put("result", JSON.parseObject(returnMsg));
			map.put("status", true);
		} catch (Exception e) {
			log.error("同步交易状态出错", e);
			map.put("status", false);
			map.put("msg", "同步交易状态失败");
		}
		return map;
	}

	/**
	 * 查询交易成功、记账失败的订单
	 * @param yesDate
	 * @return
	 */
	@Override
	public List<CollectiveTransOrder> selectRecordAccountFail(String yesDate) {
		return transInfoDao.selectRecordAccountFail(yesDate);
	}

	@Override
	public String queryAcqTerminalNo(String acqMerchantNo) {
		return transInfoDao.queryAcqTerminalNo(acqMerchantNo);
	}

	@Override
	public ShareSettleInfo shareSettleInfo(String orderNo) {
		return transInfoDao.shareSettleInfo(orderNo);
	}

	@Override
	public CollectiveTransOrder getCtoBySurvey(String orderNo) {
		return transInfoDao.getCtoBySurvey(orderNo);
	}

	@Override
	public ZqMerchantInfo getAcqMerchant(String uniMerNo) {
		return transInfoDao.getAcqMerchant(uniMerNo);
	}

	@Override
	public AcqTerminalStore getAcqTer(String uniMerNo) {
		return transInfoDao.getAcqTer(uniMerNo);
	}

	@Override
	public void accountRecordBatch(String ids, Map<String, Object> msg) {
		if(ids!=null&&!"".equals(ids)) {
			String[] strs = ids.split(",");
			int successNum=0;
			if (strs != null && strs.length > 0) {
				for(String id:strs){
					CollectiveTransOrder info = transInfoDao.queryCollectiveTransOrder(id);
					if("SUCCESS".equals(info.getTransStatus())&&!"1".equals(info.getAccount())&&info.getAcqOrgId()!=null){
						int sta=accountRecordService.accountRecordInfo(info);
						if(sta>0){
							successNum++;
						}
					}
				}
			}
			msg.put("status", true);
			msg.put("msg", "批量记账,选中"+strs.length+"条数据,成功记账"+successNum+"条!");
			return;
		}else{
			msg.put("status", false);
			msg.put("msg", "批量记账失败,无数据!");
			return;
		}
	}

}
