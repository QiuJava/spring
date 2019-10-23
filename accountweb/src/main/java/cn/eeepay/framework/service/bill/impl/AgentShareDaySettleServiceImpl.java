package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.EnterAccountStatus;
import cn.eeepay.framework.model.bill.*;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.service.bill.*;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service("agentShareDaySettleService")
public class AgentShareDaySettleServiceImpl implements AgentShareDaySettleService{
	private static final Logger log = LoggerFactory.getLogger(AgentShareDaySettleServiceImpl.class);
	@Resource
	public AgentShareDaySettleMapper agentShareDaySettleMapper;
	@Resource
	public GenericTableService genericTableService;
	@Resource
	public TransShortInfoService transShortInfoService;
	@Resource
	public AgentPreRecordTotalService agentPreRecordTotalService;
	@Resource
	public DuiAccountDetailService duiAccountDetailService;
	@Resource
	public ExtAccountService extAccountService;
	@Resource
	public SysDictService sysDictService;
	@Resource
	public AgentPreFreezeService agentPreFreezeService;
	@Resource
	public AgentInfoService agentInfoService;
	@Resource
	public AgentShareDaySettleService agentShareDaySettleService;
	@Resource
	public AgentProfitEnterAccountService agentProfitEnterAccountService;




	
	
//	@Value("${accountApi.http.url}")
//	private String accountApiHttpUrl;
//
//	@Value("${accountApi.http.secret}")
//	private String accountApiHttpSecret;
	
	@Override
	public int insertAgentShareDaySettle(AgentShareDaySettle agentShareDaySettle) {
		return agentShareDaySettleMapper.insertAgentShareDaySettle(agentShareDaySettle);
	}
	@Override
	public int updateAgentShareDaySettle(AgentShareDaySettle agentShareDaySettle) {
		return agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
	}
	@Override
	public int deleteAgentShareDaySettle(Integer id) {
		return agentShareDaySettleMapper.deleteAgentShareDaySettle(id);
	}
	@Override
	public List<AgentShareDaySettle> findAllAgentShareDaySettle() {
		return agentShareDaySettleMapper.findAllAgentShareDaySettle();
	}
	@Override
	public List<AgentShareDaySettle> findAgentShareDaySettleList(AgentShareDaySettle agentShareDaySettle,Sort sort,
			Page<AgentShareDaySettle> page) {
		if (StringUtils.isNotBlank(agentShareDaySettle.getTransDate1())) {
			agentShareDaySettle.setTransDate1(agentShareDaySettle.getTransDate1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentShareDaySettle.getTransDate2())) {
			agentShareDaySettle.setTransDate2(agentShareDaySettle.getTransDate2() + " 23:59:59");
		}
		if (StringUtils.isNotBlank(agentShareDaySettle.getGroupTime1())) {
			agentShareDaySettle.setGroupTime1(agentShareDaySettle.getGroupTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentShareDaySettle.getGroupTime2())) {
			agentShareDaySettle.setGroupTime2(agentShareDaySettle.getGroupTime2() + " 23:59:59");
		}
		
		List<AgentShareDaySettle>  agentShareDaySettleList = agentShareDaySettleMapper.findAgentShareDaySettleList(agentShareDaySettle, sort, page);
		//List<AgentShareDaySettle> pageResultList = page.getResult();
		for (AgentShareDaySettle agentShareDaySettle2 : agentShareDaySettleList) {
			String parentAgentNo = agentShareDaySettle2.getParentAgentNo();
			if (parentAgentNo.equals("0")) {
				agentShareDaySettle2.setParentAgentNo("");
				agentShareDaySettle2.setParentAgentName("");
			}
			else{
				AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(parentAgentNo);
				agentShareDaySettle2.setParentAgentName(agentInfo.getAgentName());
			}
		}
		return agentShareDaySettleList;
	}
	@Override
	public AgentShareDaySettle findAgentShareDaySettleById(Integer id) {
		return agentShareDaySettleMapper.findAgentShareDaySettleById(id);
	}
	
	@Override
	public Map<String, Object> validateAgentProfitCollection(String transDate1) throws Exception {
		Map<String,Object> msg=new HashMap<>();
	    List<DuiAccountDetail> duiAccountDetailList = duiAccountDetailService.findNoCheckedDuiAccountDetailListByTransTime(transDate1);
	    if (duiAccountDetailList != null) {
			int size = duiAccountDetailList.size();
			if (size > 0) {
				msg.put("status", false);
				String resultMsg = "有"+size+"笔交易未对账，是否继续汇总？";
				msg.put("msg", resultMsg);
				return msg;
			}
		}
	    msg.put("status", true);
		msg.put("msg", "执行成功");
		return msg;
	}
	
	@Override
	@Transactional
	public Map<String,Object> agentProfitCollection(String transDate1,String operater) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		String sysKey = "sys_agents_profit";
		String sysName = "enter_scale";
		SysDict sysDict = sysDictService.findSysDictByKeyName(sysKey, sysName);
		BigDecimal enterScale = new BigDecimal(sysDict.getSysValue());
//		List<DuiAccountDetail> duiAccountDetailList = duiAccountDetailService.findDuiAccountDetailListByTransTime(transDate1);
//		for (DuiAccountDetail duiAccountDetail : duiAccountDetailList) {
//			if ("NO_CHECKED".equals(duiAccountDetail.getCheckAccountStatus())) {
//				msg.put("status", false);
//				String resultMsg = "交易订单"+duiAccountDetail.getPlateOrderNo() + ",收单机构"+duiAccountDetail.getAcqEnname() +",未对账";
//				msg.put("msg", resultMsg);
//				return msg;
//			}
//		}
		Date groupTime = new Date();
		String groupTimeStr = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",groupTime);
		String collectionBatchNo = genericTableService.createAgentProfitCollectionBatchNo();
		log.info("代理商分润汇总批次号："+collectionBatchNo);
		//List<AgentShareDaySettle> agentShareDaySettleList= findCollectionGropByAgent(transDate1);
		
		List<AgentShareDaySettle> agentShareDaySettleList = new ArrayList<>();
		Map<String, String> params = new HashMap<>();
		String date1 = transDate1+" 00:00:00";
		String date2 = transDate1+" 23:59:59";
		params.put("transDate1", date1);
		params.put("transDate2", date2);
		List<TransShortInfo> transShortInfoList2 = transShortInfoService.findNoCollectTransShortInfo(params);
		
		if (transShortInfoList2.size() < 1) {
			msg.put("status", false);
			String resultMsg = "无未汇总的分润明细记录";
			msg.put("msg", resultMsg);
			return msg;
		}
//		List<AgentInfo> allAgentInfoList = agentInfoService.findAllAgentInfoList();
		for(Integer i=1; i<=20; i++){
			String agentLevel = i.toString();
			List<AgentInfo> agentInfoList = agentInfoService.findEntityByLevel(agentLevel);
			for (AgentInfo agentInfo : agentInfoList) {
				Map<String, String> params1 = new HashMap<>();
				params1.put("transDate1", transDate1);
				params1.put("agentNode", agentInfo.getAgentNode());
				params1.put("agentLevel", agentLevel);
				params1.put("date1",date1);
				params1.put("date2",date2);
				AgentShareDaySettle asds = this.findNoCollectTransShortInfoByAgentNodeAndLevel(params1);
//				AgentShareDaySettle asds = new AgentShareDaySettle();
				String agentNo = agentInfo.getAgentNo();
				String agentNode = agentInfo.getAgentNode();
				String agentName = agentInfo.getAgentName();
//				String oneAgentNo="";
//				String oneAgentName="";
//				for (AgentInfo allAgentInfo : allAgentInfoList) {
//					if (agentInfo.getOneLevelId().equals(oneAgentInfo.getId())) {
//						oneAgentNo = oneAgentInfo.getAgentNo();
//						oneAgentName = oneAgentInfo.getAgentName();
//						break;
//					}
//				}
//				BigDecimal profits = BigDecimal.ZERO;
//				BigDecimal settle_profits = BigDecimal.ZERO;
				
//				String parentAgentNo="";
//				String saleName="";
//				Date transDate = null;
				
				
				
				Map<String, Object> map1  = agentShareDaySettleService.findNoCollectTransShortInfoGroupByAgentNodeAndLevel(params1);
				Integer transTotalNum =  (Integer) map1.get("transTotalNum");
				
				if (asds != null) {
//					AgentInfo parentAgentInfo = agentInfoService.findEntityById(agentInfo.getParentId());
//					
//					if (parentAgentInfo != null) {
//						asds.setParentAgentNo(parentAgentInfo.getAgentNo());
//					}
					
					if (agentLevel.equals("1")) {
						asds.setParentAgentNo("0");
					}
					else{
						asds.setParentAgentNo(agentInfo.getParentId());
					}
					
					asds.setAgentNo(agentNo);
					asds.setAgentName(agentName);
					asds.setAgentNode(agentNode);
					asds.setAgentLevel(agentLevel);
//					asds.setOneAgentNo(oneAgentNo);
//					asds.setOneAgentName(oneAgentName);
					asds.setTransTotalNum(transTotalNum);
					agentShareDaySettleList.add(asds);
				}

			}
			
		}
		
		List<AgentPreFreeze> agentPreFreezeList = new ArrayList<>();
		List<AgentPreRecordTotal> updateAgentPreRecordTotalList = new ArrayList<>();
		List<AgentPreRecordTotal> insertAgentPreRecordTotalList = new ArrayList<>();
		for (AgentShareDaySettle agentShareDaySettle : agentShareDaySettleList) {
			String agentNo = agentShareDaySettle.getAgentNo();
			String agentName = agentShareDaySettle.getAgentName();
			agentShareDaySettle.setCollectionBatchNo(collectionBatchNo);
			agentShareDaySettle.setGroupTime(groupTime);
			BigDecimal preTransShareAmount = agentShareDaySettle.getPreTransShareAmount();
			if (preTransShareAmount == null) {
				preTransShareAmount = BigDecimal.ZERO;
			}
			BigDecimal preTransCashAmount = agentShareDaySettle.getPreTransCashAmount();
			if (preTransCashAmount == null) {
				preTransCashAmount = BigDecimal.ZERO;
			}
			BigDecimal preTotalTransAmount = preTransShareAmount.add(preTransCashAmount);
			agentShareDaySettle.setAdjustTransShareAmount(preTransShareAmount);
			agentShareDaySettle.setAdjustTransCashAmount(preTransCashAmount);
			BigDecimal adjustTotalShareAmount = preTransShareAmount.add(preTransCashAmount);
			agentShareDaySettle.setAdjustTotalShareAmount(adjustTotalShareAmount);
			agentShareDaySettle.setRealEnterShareAmount(BigDecimal.ZERO);
			agentShareDaySettle.setTuiCostAmount(BigDecimal.ZERO);
			agentShareDaySettle.setOpenBackAmount(BigDecimal.ZERO);
			agentShareDaySettle.setRateDiffAmount(BigDecimal.ZERO);
			agentShareDaySettle.setRiskSubAmount(BigDecimal.ZERO);
			agentShareDaySettle.setBailSubAmount(BigDecimal.ZERO);
			agentShareDaySettle.setMerMgAmount(BigDecimal.ZERO);
			agentShareDaySettle.setOtherAmount(BigDecimal.ZERO);
			
			AgentPreFreeze agentPreFreeze = new AgentPreFreeze();
			agentPreFreeze.setAgentNo(agentNo);
			agentPreFreeze.setAgentName(agentName);
			agentPreFreeze.setFreezeTime(new Date());
			agentPreFreeze.setOperater(operater);
			BigDecimal otherFreezeAmount = preTotalTransAmount.multiply(new BigDecimal(1).subtract(enterScale.divide(new BigDecimal(100))));
			agentPreFreeze.setOtherFreezeAmount(otherFreezeAmount);
			agentPreFreeze.setTerminalFreezeAmount(BigDecimal.ZERO);
			String remark = "分润待付款" + DateUtil.getDefaultFormatDate(agentShareDaySettle.getTransDate());
			agentPreFreeze.setRemark(remark);
			if (otherFreezeAmount.compareTo(BigDecimal.ZERO) > 0) {
				agentPreFreezeList.add(agentPreFreeze);
			
				AgentPreRecordTotal  existAgentPreRecordTotal = agentPreRecordTotalService.findAgentPreRecordTotalByAgentNo(agentNo);
				
				if (existAgentPreRecordTotal != null) {
					existAgentPreRecordTotal.setOtherFreezeAmount(existAgentPreRecordTotal.getOtherFreezeAmount().add(otherFreezeAmount));
					updateAgentPreRecordTotalList.add(existAgentPreRecordTotal);
				}
				else{
					AgentPreRecordTotal agentPreRecordTotal = new AgentPreRecordTotal();
					agentPreRecordTotal.setAgentNo(agentNo);
					agentPreRecordTotal.setAgentName(agentName);
					agentPreRecordTotal.setTerminalFreezeAmount(BigDecimal.ZERO);
					agentPreRecordTotal.setOtherFreezeAmount(otherFreezeAmount);
					insertAgentPreRecordTotalList.add(agentPreRecordTotal);
				}
			}
		}
		Map<String,Object> result = insertAgentShareDaySettleSplitBatch(agentShareDaySettleList);
		
		agentPreFreezeService.insertAgentPreFreezeSplitBatch(agentPreFreezeList);
		
		agentPreRecordTotalService.updateAgentPreRecordTotalSplitBatch(updateAgentPreRecordTotalList);
		agentPreRecordTotalService.insertAgentPreRecordTotalSplitBatch(insertAgentPreRecordTotalList);
		
		
		Boolean resultStatus = (Boolean) result.get("status");
		if (resultStatus) {

			//不知道为什么以前要分批update，难道是数据量太大？
//			List<TransShortInfo> transShortInfoList = transShortInfoService.findAllTransShortInfoByTransTime(transDate1);
//			for (TransShortInfo transShortInfo : transShortInfoList) {
//				transShortInfo.setCollectionBatchNo(collectionBatchNo);
//				transShortInfo.setAgentProfitCollectionStatus("COLLECTED");
//				transShortInfo.setAgentProfitGroupTime(groupTime);
//			}
//			Map<String,Object> result2 = transShortInfoService.updateTransShortInfoSplitBatch(transShortInfoList);
//			Boolean resultStatus2 = (Boolean) result2.get("status");

			Map<String,String> map = new HashMap<>();
			map.put("collectionBatchNo",collectionBatchNo);
			map.put("groupTime",groupTimeStr);
			map.put("date1",date1);
			map.put("date2",date2);

			int result1 = transShortInfoService.updateTransShortInfoByDate(map);

			if (result1>0) {
				msg.put("status", true);
				msg.put("msg", "执行成功");
				return msg;
			}else{
				throw new Exception("代理商分润汇总异常");
			}
		}
		return msg;
	}
	@Override
	public List<AgentShareDaySettle> findCollectionGropByAgent(String transDate1) {
		Map<String, String> params = new HashMap<>();
		params.put("transDate1", transDate1);
		return agentShareDaySettleMapper.findCollectionGropByAgent(params);
	}
	@Override
	@Transactional
	public Map<String,Object> insertAgentShareDaySettleSplitBatch(List<AgentShareDaySettle> agentShareDaySettleList) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		int i = 0;
		int batchCount = 200;
		List<AgentShareDaySettle> asdList = new ArrayList<>();
		List<List<?>> agentShareDaySettleSplitList = ListUtil.batchList(agentShareDaySettleList, batchCount);
		for (List<?> clist : agentShareDaySettleSplitList) {
			for (Object object : clist) {
				AgentShareDaySettle asd = (AgentShareDaySettle) object;
				if (asd.getMerCashFee() == null) {
					asd.setMerCashFee(BigDecimal.ZERO);
				}
				if (asd.getDeductionFee() == null) {
					asd.setDeductionFee(BigDecimal.ZERO);
				}
				if (asd.getDaiCost() == null) {
					asd.setDaiCost(BigDecimal.ZERO);
				}
				if (asd.getDianCost() == null) {
					asd.setDianCost(BigDecimal.ZERO);
				}
				if (asd.getAcqOutProfit() == null) {
					asd.setAcqOutProfit(BigDecimal.ZERO);
				}
				if (asd.getPreTransShareAmount() == null) {
					asd.setPreTransShareAmount(BigDecimal.ZERO);
				}
				if (asd.getPreTransCashAmount() == null) {
					asd.setPreTransCashAmount(BigDecimal.ZERO);
				}
				if (asd.getOpenBackAmount() == null) {
					asd.setOpenBackAmount(BigDecimal.ZERO);
				}
				if (asd.getRateDiffAmount() == null) {
					asd.setRateDiffAmount(BigDecimal.ZERO);
				}
				if (asd.getRiskSubAmount() == null) {
					asd.setRiskSubAmount(BigDecimal.ZERO);
				}
				if (asd.getBailSubAmount() == null) {
					asd.setBailSubAmount(BigDecimal.ZERO);
				}
				if (asd.getMerMgAmount() == null) {
					asd.setMerMgAmount(BigDecimal.ZERO);
				}
				if (asd.getOtherAmount() == null) {
					asd.setOtherAmount(BigDecimal.ZERO);
				}
				if (asd.getAdjustTransShareAmount() == null) {
					asd.setAdjustTransShareAmount(BigDecimal.ZERO);
				}
				if (asd.getAdjustTransCashAmount() == null) {
					asd.setAdjustTransCashAmount(BigDecimal.ZERO);
				}
				if (asd.getAdjustTotalShareAmount() == null) {
					asd.setAdjustTotalShareAmount(BigDecimal.ZERO);
				}
				if (asd.getTerminalFreezeAmount() == null) {
					asd.setTerminalFreezeAmount(BigDecimal.ZERO);
				}
				if (asd.getOtherFreezeAmount() == null) {
					asd.setOtherFreezeAmount(BigDecimal.ZERO);
				}
				if (asd.getEnterAccountStatus() == null) {
					asd.setEnterAccountStatus("NOENTERACCOUNT");
				}
				asdList.add(asd);
			}
			if (asdList.size() > 0) {
				log.info("插入代理商分润日结表{}条",asdList.size());
				int j = agentShareDaySettleMapper.insertAgentShareDaySettleBatch(asdList);
				if (j > 0) {
					i = i + j;
				}
				if (!asdList.isEmpty()) {
					asdList.clear();
				}
			}
		}
		if (i > 0) {
			msg.put("status", true);
			msg.put("msg", "执行成功");
		}
		else{
			msg.put("status", true);
			msg.put("msg", "没有任何数据插入");
		}
		return msg;
	}
	@Override
	@Transactional
	public Map<String, Object> agentProfitTryCalculation(String transDate1) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		TryCalculationResult tryCalculationResult = new TryCalculationResult();
		List<AgentShareDaySettle> agentShareDaySettleList= agentShareDaySettleMapper.findAllAgentShareDaySettleByEnterAccountStatusAndTransDate("NOENTERACCOUNT",transDate1);
		for (AgentShareDaySettle agentShareDaySettle : agentShareDaySettleList) {
			BigDecimal preTransShareAmount = agentShareDaySettle.getPreTransShareAmount();
			BigDecimal preTransCashAmount = agentShareDaySettle.getPreTransCashAmount();
			BigDecimal openBackAmount = BigDecimal.ZERO;
			BigDecimal rateDiffAmount = BigDecimal.ZERO;
			BigDecimal tuiCostAmount = BigDecimal.ZERO;
			BigDecimal riskSubAmount = BigDecimal.ZERO;
			BigDecimal merMgAmount = BigDecimal.ZERO;
			BigDecimal bailSubAmount = BigDecimal.ZERO;
			BigDecimal otherAmount = BigDecimal.ZERO;
			String agentNo = agentShareDaySettle.getAgentNo();
			AgentPreRecordTotal agentPreRecordTotal = agentPreRecordTotalService.findAgentPreRecordTotalByAgentNo(agentNo);
			if (agentPreRecordTotal != null) {
				openBackAmount = agentPreRecordTotal.getOpenBackAmount();
				if (openBackAmount == null)  openBackAmount = BigDecimal.ZERO;
				rateDiffAmount = agentPreRecordTotal.getRateDiffAmount();
				if (rateDiffAmount == null)  rateDiffAmount = BigDecimal.ZERO;
				tuiCostAmount = agentPreRecordTotal.getTuiCostAmount();
				if (tuiCostAmount == null)  tuiCostAmount = BigDecimal.ZERO;
				riskSubAmount = agentPreRecordTotal.getRiskSubAmount();
				if (riskSubAmount == null)  riskSubAmount = BigDecimal.ZERO;
				merMgAmount = agentPreRecordTotal.getMerMgAmount();
				if (merMgAmount == null)  merMgAmount = BigDecimal.ZERO;
				bailSubAmount = agentPreRecordTotal.getBailSubAmount();
				if (bailSubAmount == null)  bailSubAmount = BigDecimal.ZERO;
				otherAmount = agentPreRecordTotal.getOtherAmount();
				if (otherAmount == null)  otherAmount = BigDecimal.ZERO;
			}
			agentShareDaySettle.setOpenBackAmount(openBackAmount);
			agentShareDaySettle.setRateDiffAmount(rateDiffAmount);
			preTransShareAmount = preTransShareAmount.add(openBackAmount).add(rateDiffAmount);
			tryCalculationResult = tryCalculationFunction(preTransShareAmount,preTransCashAmount,tuiCostAmount);//对比超级推成本
			Boolean resultStatus = tryCalculationResult.isStatus();
			if (resultStatus == false) {
				agentShareDaySettle.setTuiCostAmount(tryCalculationResult.getAmount());
				agentShareDaySettle.setRiskSubAmount(BigDecimal.ZERO);
				agentShareDaySettle.setBailSubAmount(BigDecimal.ZERO);
				agentShareDaySettle.setMerMgAmount(BigDecimal.ZERO);
				agentShareDaySettle.setOtherAmount(BigDecimal.ZERO);
				agentShareDaySettle.setAdjustTransShareAmount(tryCalculationResult.getAdjustTransShareAmount());
				agentShareDaySettle.setAdjustTransCashAmount(tryCalculationResult.getAdjustTransCashAmount());
			}else{
				agentShareDaySettle.setTuiCostAmount(tryCalculationResult.getAmount());
				tryCalculationResult = tryCalculationFunction(tryCalculationResult.getAdjustTransShareAmount(),tryCalculationResult.getAdjustTransCashAmount(),riskSubAmount);//对比风控扣款
				resultStatus = tryCalculationResult.isStatus();
				if (resultStatus == false) {
					agentShareDaySettle.setRiskSubAmount(tryCalculationResult.getAmount());
					agentShareDaySettle.setBailSubAmount(BigDecimal.ZERO);
					agentShareDaySettle.setMerMgAmount(BigDecimal.ZERO);
					agentShareDaySettle.setOtherAmount(BigDecimal.ZERO);
					agentShareDaySettle.setAdjustTransShareAmount(tryCalculationResult.getAdjustTransShareAmount());
					agentShareDaySettle.setAdjustTransCashAmount(tryCalculationResult.getAdjustTransCashAmount());
				}else{
					agentShareDaySettle.setRiskSubAmount(tryCalculationResult.getAmount());
					//对比商户管理费
					tryCalculationResult = tryCalculationFunction(tryCalculationResult.getAdjustTransShareAmount(),tryCalculationResult.getAdjustTransCashAmount(),merMgAmount);
					Boolean resultStatus2 = tryCalculationResult.isStatus();
					if (resultStatus2 == false) {
						agentShareDaySettle.setBailSubAmount(BigDecimal.ZERO);
						agentShareDaySettle.setMerMgAmount(tryCalculationResult.getAmount());
						agentShareDaySettle.setOtherAmount(BigDecimal.ZERO);
						agentShareDaySettle.setAdjustTransShareAmount(tryCalculationResult.getAdjustTransShareAmount());
						agentShareDaySettle.setAdjustTransCashAmount(tryCalculationResult.getAdjustTransCashAmount());
					}
					else{
						agentShareDaySettle.setMerMgAmount(tryCalculationResult.getAmount());
						//对比保证金扣款
						tryCalculationResult = tryCalculationFunction(tryCalculationResult.getAdjustTransShareAmount(),tryCalculationResult.getAdjustTransCashAmount(),bailSubAmount);
						Boolean resultStatus3 = tryCalculationResult.isStatus();
						if (resultStatus3 == false) {
							agentShareDaySettle.setBailSubAmount(tryCalculationResult.getAmount());
							agentShareDaySettle.setOtherAmount(BigDecimal.ZERO);
							agentShareDaySettle.setAdjustTransShareAmount(tryCalculationResult.getAdjustTransShareAmount());
							agentShareDaySettle.setAdjustTransCashAmount(tryCalculationResult.getAdjustTransCashAmount());
						}
						else{
							agentShareDaySettle.setBailSubAmount(tryCalculationResult.getAmount());
							//对比其他
							tryCalculationResult = tryCalculationFunction(tryCalculationResult.getAdjustTransShareAmount(),tryCalculationResult.getAdjustTransCashAmount(),otherAmount);
							Boolean resultStatus4 = tryCalculationResult.isStatus();
							if (resultStatus4 == false) {
								agentShareDaySettle.setOtherAmount(tryCalculationResult.getAmount());
								agentShareDaySettle.setAdjustTransShareAmount(tryCalculationResult.getAdjustTransShareAmount());
								agentShareDaySettle.setAdjustTransCashAmount(tryCalculationResult.getAdjustTransCashAmount());
							}
							else{
								agentShareDaySettle.setOtherAmount(tryCalculationResult.getAmount());
								agentShareDaySettle.setAdjustTransShareAmount(tryCalculationResult.getAdjustTransShareAmount());
								agentShareDaySettle.setAdjustTransCashAmount(tryCalculationResult.getAdjustTransCashAmount());
							}
						}
					}
				}
			}
			agentShareDaySettle.setAdjustTotalShareAmount(agentShareDaySettle.getAdjustTransShareAmount().add(agentShareDaySettle.getAdjustTransCashAmount()));
			agentShareDaySettle = tryCalculationFreezeFunction(agentShareDaySettle,agentPreRecordTotal);
			String agentShareDaySettleBean = ReflectionToStringBuilder.toString(agentShareDaySettle);
			log.info("agentShareDaySettleBean = {}",agentShareDaySettleBean);
			agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
		}
		if (agentShareDaySettleList.size() > 0) {
			msg.put("status", true);
			msg.put("msg", "执行成功");
			return msg;
		}
		else{
			msg.put("status", false);
			msg.put("msg", "没有试算的数据");
			return msg;
		}
	}
	
	/**
	 * 计算分润复制函数
	 * @param x1 原交易分润
	 * @param x2 原提现分润
	 * @param y 当前比较的金额
	 * @return 是否够扣,需要复制过来的值
	 */
	private TryCalculationResult tryCalculationFunction(BigDecimal x1,BigDecimal x2,BigDecimal y){
		TryCalculationResult tryCalculationResult = new TryCalculationResult();
		if (x1.add(y).compareTo(BigDecimal.ZERO) >= 0) {
			tryCalculationResult.setStatus(true);
			tryCalculationResult.setAmount(y);
			tryCalculationResult.setAdjustTransShareAmount(x1.add(y));
			tryCalculationResult.setAdjustTransCashAmount(x2);
			return tryCalculationResult;
		}
		BigDecimal x = x1.add(x2);
				
		if (x.add(y).compareTo(BigDecimal.ZERO) >= 0) {
			tryCalculationResult.setStatus(true);
			tryCalculationResult.setAmount(y);
			tryCalculationResult.setAdjustTransShareAmount(BigDecimal.ZERO);
			tryCalculationResult.setAdjustTransCashAmount(x.add(y));
			return tryCalculationResult;
		}
		y = x.multiply(new BigDecimal(-1));
		tryCalculationResult.setStatus(false);
		tryCalculationResult.setAmount(y);
		tryCalculationResult.setAdjustTransShareAmount(BigDecimal.ZERO);
		tryCalculationResult.setAdjustTransCashAmount(BigDecimal.ZERO);
		return tryCalculationResult;
		
	}
	
	/**
	 * 试算冻结函数
	 * @param agentShareDaySettle
	 * @param agentPreRecordTotal
	 * @return
	 */
	private AgentShareDaySettle tryCalculationFreezeFunction(AgentShareDaySettle agentShareDaySettle,AgentPreRecordTotal agentPreRecordTotal){
		BigDecimal adjustTotalShareAmount = agentShareDaySettle.getAdjustTotalShareAmount();
		BigDecimal terminalFreezeAmount = BigDecimal.ZERO;
		BigDecimal otherFreezeAmount = BigDecimal.ZERO;
		if (agentPreRecordTotal != null) {
			terminalFreezeAmount = agentPreRecordTotal.getTerminalFreezeAmount();
			otherFreezeAmount = agentPreRecordTotal.getOtherFreezeAmount();
			if (terminalFreezeAmount == null) {
				terminalFreezeAmount = BigDecimal.ZERO;
			}
			if (otherFreezeAmount == null) {
				otherFreezeAmount = BigDecimal.ZERO;
			}
		}
		if (adjustTotalShareAmount.compareTo(terminalFreezeAmount) >= 0) {
			agentShareDaySettle.setTerminalFreezeAmount(terminalFreezeAmount);
			adjustTotalShareAmount = adjustTotalShareAmount.subtract(terminalFreezeAmount);
			if (adjustTotalShareAmount.compareTo(otherFreezeAmount) >= 0) {
				agentShareDaySettle.setOtherFreezeAmount(otherFreezeAmount);
			}
			else{
				agentShareDaySettle.setOtherFreezeAmount(adjustTotalShareAmount);
			}
		}
		else{
			agentShareDaySettle.setTerminalFreezeAmount(adjustTotalShareAmount);
			agentShareDaySettle.setOtherFreezeAmount(BigDecimal.ZERO);
		}
		return agentShareDaySettle;
	}
	@Override
	public Map<String, Object> agentProfitEnterAccount(String transDate1) throws Exception {

		// 获取到登录者信息
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Map<String,Object> msg=new HashMap<>();
		for(int i= 1 ; i <= 20; i++){
			List<AgentInfo> agentInfoSwitchList = agentInfoService.findEntityByLevelSwitch(i);
			
			//List<AgentShareDaySettle> oneAgentShareDaySettleList = agentShareDaySettleMapper.findNoEnterOneEntityByTransDateAndLevel(transDate1,i);
//			if (oneAgentShareDaySettleList != null && oneAgentShareDaySettleList.size() < 1) {
//				break;
//			}
			for (AgentInfo agentInfoSwitch : agentInfoSwitchList) {
				String agentNo = agentInfoSwitch.getAgentNo();
				List<AgentShareDaySettle> oneAgentShareDaySettleList = agentShareDaySettleMapper.findNoEnterOneEntityByTransDateAndAgentNo(transDate1, agentNo);
				for (AgentShareDaySettle agentShareDaySettle : oneAgentShareDaySettleList) {
					try {
						String parentAgentNo = agentShareDaySettle.getParentAgentNo();
						String batchNo = agentShareDaySettle.getCollectionBatchNo();
						String agentLevel = agentShareDaySettle.getAgentLevel();
						AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
						
						if ("0".equals(agentInfo.getProfitSwitch())) {
							String s = String.format("代理商%s分润状态开关为关闭,不用入账", agentNo);
							msg.put("status", false);
							msg.put("msg", s);
							log.info(msg.toString());
							agentShareDaySettle.setEnterAccountMessage(s);
							agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
							continue;
						}
						//上级已经入账
						if (parentAgentNo != null && !parentAgentNo.equals("0")) {
							AgentShareDaySettle parentAsds = agentShareDaySettleMapper.findEntityByBatchNoAndAgentNo(batchNo, parentAgentNo);
							if (!parentAsds.getEnterAccountStatus().equals(EnterAccountStatus.ENTERACCOUNTED.toString())) {
								String s = String.format("代理商%s的上一级代理商%s没有入账,不用入账", agentNo,parentAgentNo);
								msg.put("status", false);
								msg.put("msg", s);
								log.info(msg.toString());
								
								agentShareDaySettle.setEnterAccountMessage(s);
								agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
								continue;
							}
						}
						
						if (!agentLevel.equals("1")) {
							List<AgentInfo> openDirectList = agentInfoService.findOpenDirectEntityByParentAgentNo(parentAgentNo);
							AgentShareDaySettle parentAgentShareDaySettle = agentShareDaySettleMapper.findEntityByBatchNoAndAgentNo(batchNo, parentAgentNo);
							BigDecimal allAdjustTotalShareAmount = BigDecimal.ZERO; 
							for (AgentInfo openAgentInfo : openDirectList) {
								AgentShareDaySettle asds = agentShareDaySettleMapper.findEntityByBatchNoAndAgentNo(batchNo, openAgentInfo.getAgentNo());
								if (asds != null) {
									allAdjustTotalShareAmount = allAdjustTotalShareAmount.add(asds.getAdjustTotalShareAmount());
								}
							}
							
							BigDecimal parentAgentTotalAdjustShareAmount = parentAgentShareDaySettle.getAdjustTotalShareAmount().subtract(parentAgentShareDaySettle.getTerminalFreezeAmount()).subtract(parentAgentShareDaySettle.getOtherFreezeAmount());
							log.info("currentAgentTotalAdjustShareAmount="+parentAgentTotalAdjustShareAmount);
							log.info("allAdjustTotalShareAmount="+allAdjustTotalShareAmount);
							if (parentAgentTotalAdjustShareAmount.compareTo(allAdjustTotalShareAmount) < 0) {
								String s = String.format("上级代理商%s分润小于所有直接下级代理商调整后分润总额总和,不用入账", parentAgentNo);
								msg.put("status", false);
								msg.put("msg", s);
								log.info(msg.toString());
								
								agentShareDaySettle.setEnterAccountMessage(s);
								agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
								continue;
							}
						}
						agentShareDaySettle.setOperator(userInfo.getUsername());
						Map<String,Object> result =  agentProfitEnterAccountService.agentProfitEnterAccount(agentShareDaySettle);
						Boolean resultStatus =  (Boolean) result.get("status");
						String resultMsg =  (String) result.get("msg");
						if (resultStatus) {
							msg.put("status", resultStatus);
							msg.put("msg", resultMsg);
							log.info(msg.toString());
						}
						else{
							msg.put("status", resultStatus);
							msg.put("msg", resultMsg);
							log.info(msg.toString());
						}
					} catch (Exception e) {
						log.info("异常:"+e);
						String s = String.format("代理商%s入账异常:"+ e.getMessage(), agentNo);
						log.info(s);
						agentShareDaySettle.setEnterAccountMessage(s);
						agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
					}
				}
			}
		}
		msg.put("status", true);
		msg.put("msg", "操作成功");
		log.info(msg.toString());
		return msg;
	}
	
	/**
	 * 
	 * @param agentShareDaySettleParams
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> agentProfitEnterAccount2(AgentShareDaySettle agentShareDaySettleParams) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		String agentNode = agentShareDaySettleParams.getAgentNode();
		String agentLevel = agentShareDaySettleParams.getAgentLevel();
		String batchNo = agentShareDaySettleParams.getCollectionBatchNo();
		if (agentLevel == null) {
			agentLevel = "1";
		}
		// 获取到登录者信息
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Integer level = Integer.valueOf(agentLevel);
		for(int i= level ; i <= 20; i++ ){
			List<AgentShareDaySettle> oneAgentShareDaySettleList = agentShareDaySettleMapper.findNoEnterOneEntityByLikeAgentNodeAndLevel(batchNo,agentNode,i);
			for (AgentShareDaySettle agentShareDaySettle : oneAgentShareDaySettleList) {
				String agentNo = agentShareDaySettle.getAgentNo();
				try{
					String parentAgentNo = agentShareDaySettle.getParentAgentNo();
					String currentAgentLevel = agentShareDaySettle.getAgentLevel();
					AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
					
					if ("0".equals(agentInfo.getProfitSwitch())) {
						
						String s  = String.format("代理商%s分润状态开关为关闭,不用入账", agentNo);
						msg.put("status", false);
						msg.put("msg", s);
						log.info(msg.toString());
						
						agentShareDaySettle.setEnterAccountMessage(s);
						agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
						continue;
					}
					//上级已经入账
					if (parentAgentNo != null && !parentAgentNo.equals("0")) {
						AgentShareDaySettle parentAsds = agentShareDaySettleMapper.findEntityByBatchNoAndAgentNo(batchNo, parentAgentNo);
						if (!parentAsds.getEnterAccountStatus().equals(EnterAccountStatus.ENTERACCOUNTED.toString())) {
							String s  = String.format("代理商%s的上一级代理商%s没有入账,不用入账", agentNo,parentAgentNo);
							msg.put("status", false);
							msg.put("msg", s);
							log.info(msg.toString());
							agentShareDaySettle.setEnterAccountMessage(s);
							agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
							continue;
						}
					}
					if (!currentAgentLevel.equals("1")) {
						List<AgentInfo> openDirectList = agentInfoService.findOpenDirectEntityByParentAgentNo(parentAgentNo);
						AgentShareDaySettle parentAgentShareDaySettle = agentShareDaySettleMapper.findEntityByBatchNoAndAgentNo(batchNo, parentAgentNo);
						
						BigDecimal allAdjustTotalShareAmount = BigDecimal.ZERO; 
						for (AgentInfo openAgentInfo : openDirectList) {
							AgentShareDaySettle asds = agentShareDaySettleMapper.findEntityByBatchNoAndAgentNo(batchNo, openAgentInfo.getAgentNo());
							if (asds != null) {
								allAdjustTotalShareAmount = allAdjustTotalShareAmount.add(asds.getAdjustTotalShareAmount());
							}
						}
						
						BigDecimal parentAgentTotalAdjustShareAmount = parentAgentShareDaySettle.getAdjustTotalShareAmount().subtract(parentAgentShareDaySettle.getTerminalFreezeAmount()).subtract(parentAgentShareDaySettle.getOtherFreezeAmount());
						log.info("currentAgentTotalAdjustShareAmount="+parentAgentTotalAdjustShareAmount);
						log.info("allAdjustTotalShareAmount="+allAdjustTotalShareAmount);
						if (parentAgentTotalAdjustShareAmount.compareTo(allAdjustTotalShareAmount) < 0) {
							String s  = String.format("上级代理商%s分润小于所有直接下级代理商调整后分润总额总和,不用入账", parentAgentNo);
							msg.put("status", false);
							msg.put("msg", s);
							log.info(msg.toString());
							agentShareDaySettle.setEnterAccountMessage(s);
							agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
							//return msg;
							continue;
						}
					}
					agentShareDaySettle.setOperator(userInfo.getUsername());
					Map<String,Object> result =  agentProfitEnterAccountService.agentProfitEnterAccount(agentShareDaySettle);
					Boolean resultStatus =  (Boolean) result.get("status");
					String resultMsg =  (String) result.get("msg");
					if (resultStatus) {
						msg.put("status", resultStatus);
						msg.put("msg", resultMsg);
						log.info(msg.toString());
						agentShareDaySettle.setEnterAccountMessage(resultMsg);
						agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
					}
					else{
						msg.put("status", resultStatus);
						msg.put("msg", resultMsg);
						log.info(msg.toString());
					}
				} catch (Exception e) {
					log.info("异常:"+e);
					String s = String.format("代理商%s入账异常", agentNo);
					log.info(s);
					agentShareDaySettle.setEnterAccountMessage(s);
					agentShareDaySettleMapper.updateAgentShareDaySettle(agentShareDaySettle);
				}
			}
		}
		msg.put("status", true);
		msg.put("msg", "入账完成,请检查入账状态");
		log.info(msg.toString());
		return msg;
	}
	
	/**
	 * 递归入账所有子代理商
	 * @param startAgentShareDaySettle
	 * @param isContainStart
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
	private Map<String, Object> agentProfitEnterRecursionAccount(AgentShareDaySettle startAgentShareDaySettle) throws JsonMappingException, IOException, Exception{
		Map<String,Object> msg = new HashMap<>();
		String agentNo = startAgentShareDaySettle.getAgentNo();
		String batchNo = startAgentShareDaySettle.getCollectionBatchNo();
//		Boolean isEnterChilren = true;
		
		AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
		if ("0".equals(agentInfo.getProfitSwitch())) {
			msg.put("status", false);
			msg.put("msg", String.format("代理商%s分润状态开关为关闭,不用入账", agentNo));
			log.info(msg.toString());
			return msg;
		}
		else{
			Map<String,Object> result =  agentProfitEnterAccountService.agentProfitEnterAccount(startAgentShareDaySettle);
			Boolean resultStatus =  (Boolean) result.get("status");
			String resultMsg =  (String) result.get("msg");
			if (resultStatus) {
				msg.put("status", resultStatus);
				msg.put("msg", resultMsg);
				log.info(msg.toString());
			}
			else{
				msg.put("status", resultStatus);
				msg.put("msg", resultMsg);
				log.info(msg.toString());
				return msg;
			}
			Map<String, Object> agentDirectChildrenCollection = agentShareDaySettleMapper.findAgentDirectChildrenShareDaySettleListCollection(batchNo,agentNo);
			BigDecimal allAdjustTotalShareAmount = BigDecimal.ZERO; 
			if (agentDirectChildrenCollection != null) {
				allAdjustTotalShareAmount =(BigDecimal) agentDirectChildrenCollection.get("allAdjustTotalShareAmount");
			}
			BigDecimal oneAgentTotalAdjustShareAmount = startAgentShareDaySettle.getAdjustTotalShareAmount().subtract(startAgentShareDaySettle.getTerminalFreezeAmount()).subtract(startAgentShareDaySettle.getOtherFreezeAmount());
			if (oneAgentTotalAdjustShareAmount.compareTo(allAdjustTotalShareAmount) < 0) {
				msg.put("status", false);
				msg.put("msg", String.format("代理商%s分润小于所有直接下级代理商调整后分润总额总和,不用入账", agentNo));
				log.info(msg.toString());
				return msg;
			}
		}
		List<AgentShareDaySettle> chilrenAgentShareDaySettleList= agentShareDaySettleMapper.findNoEnterDirectChilrenEntityByAgentNo(agentNo);
		for (AgentShareDaySettle asds : chilrenAgentShareDaySettleList) {
			Map<String,Object> result = agentProfitEnterRecursionAccount(asds);
			Boolean resultStatus =  (Boolean) result.get("status");
			String resultMsg =  (String) result.get("msg");
			if (resultStatus) {
				log.info(String.format("代理商%s"+resultMsg, asds.getAgentNo()));
			}
		}
		msg.put("status", true);
		msg.put("msg", "入账成功");
		log.info(msg.toString());
		return msg;
	}
	
	/**
	 * 递归入账所有子代理商
	 * @param agentShareDaySettle
	 * @return
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */
//	private Map<String, Object> childrenAgentProfitEnterAccount(AgentShareDaySettle agentShareDaySettle) throws JsonMappingException, IOException, Exception{
//		Map<String,Object> msg = new HashMap<>();
//		String agentNo = agentShareDaySettle.getAgentNo();
//		List<AgentShareDaySettle> chilrenAgentShareDaySettleList= agentShareDaySettleMapper.findNoEnterDirectChilrenEntityByAgentNo(agentNo);
//		for (AgentShareDaySettle asds : chilrenAgentShareDaySettleList) {
//			singleAgentProfitEnterAccount(asds);
//			childrenAgentProfitEnterAccount(asds);
//		}
//		msg.put("status", true);
//		msg.put("msg", "代理商分润入账成功");
//		return msg;
//	}
	
//	/**
//	 * 执行入账记账操作
//	 * @param agentShareDaySettle
//	 * @return
//	 * @throws JsonMappingException
//	 * @throws IOException
//	 * @throws Exception
//	 */
//	private Map<String, Object> agentProfitEnterAccountFunction(AgentShareDaySettle agentShareDaySettle) throws JsonMappingException, IOException, Exception {
//		Map<String,Object> msg=new HashMap<>();
//		
////		String agentNo = agentShareDaySettle.getAgentNo();
//		String agentLevel = agentShareDaySettle.getAgentLevel();
//		
////		AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
////		if ("0".equals(agentInfo.getProfitSwitch())) {
////			msg.put("status", "noNeed");
////			msg.put("msg", "代理商分润状态开关为关闭,不用入账");
////			log.info(msg.toString());
////			return msg;
////		}
//		
//
//		
//		Map<String,Object> enterAccountRecordResult = new HashMap<>();
//		if (agentLevel.equals("1")) {
//			enterAccountRecordResult = agentProfitEnterAccountRecordWithLevel1(agentShareDaySettle);
//		}
//		else{
//			enterAccountRecordResult = agentProfitEnterAccountRecordWithLevel2(agentShareDaySettle);
//		}
//		
//		Boolean enterAccountRecordResultStatus = (Boolean) enterAccountRecordResult.get("status");
//		String enterAccountRecordResultMsg = (String) enterAccountRecordResult.get("msg");
//		
//		if (enterAccountRecordResultStatus) {
//			log.info("代理商编号{} 代理商名称 {} 代理商分润入账记账 成功",agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName());
//			Map<String,Object> terminalFreezeRecordResult = agentProfitTerminalFreezeRecord(agentShareDaySettle);
//			Boolean terminalFreezeRecordResultStatus = (Boolean) terminalFreezeRecordResult.get("status");
//			String terminalFreezeRecordResultMsg = (String) terminalFreezeRecordResult.get("msg");
//			
//			if (terminalFreezeRecordResultStatus) {
//				log.info("代理商编号{} 代理商名称 {} 机具款冻结记账 成功",agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName());
//				Map<String,Object> otherFreezeRecordResult = agentProfitOtherFreezeRecord(agentShareDaySettle);
//				Boolean otherFreezeRecordResultStatus = (Boolean) otherFreezeRecordResult.get("status");
//				String otherFreezeRecordResultMsg = (String) otherFreezeRecordResult.get("msg");
//				if (otherFreezeRecordResultStatus) {
//					log.info("代理商编号{} 代理商名称 {} 其他冻结记账 成功",agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName());
//				}
//				else{
//					log.info("代理商编号{} 代理商名称 {} 其他冻结记账 记账 {}", new Object[]{agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName(),otherFreezeRecordResultMsg});
//					//1.调解冻
//					Map<String,Object> otherUnFreezeRecordResult = agentProfitOtherUnFreezeRecord(agentShareDaySettle);
//					Boolean otherUnFreezeRecordResultStatus = (Boolean) otherUnFreezeRecordResult.get("status");
//					String otherUnFreezeRecordResultMsg = (String) otherUnFreezeRecordResult.get("msg");
//					if (otherUnFreezeRecordResultStatus) {
//						log.info("代理商编号{} 代理商名称 {} 其他再解冻记账 成功",agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName());
//					}
//					else{
//						log.info("代理商编号{} 代理商名称 {} 其他再解冻记账 ",new Object[]{agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName(),otherUnFreezeRecordResultMsg});
//						
//					}
//					//2.调冲正
//					Map<String,Object> chongZhengResult = chongZheng(agentShareDaySettle);
//					Boolean chongZhengResultStatus = (Boolean) chongZhengResult.get("status");
//					String chongZhengResultMsg = (String) chongZhengResult.get("msg");
//					if (chongZhengResultStatus) {
//						log.info("代理商编号{} 代理商名称 {} 其他冲正记账 成功",agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName());
//					}
//					else{
//						log.info("代理商编号{} 代理商名称 {} 其他冲正记账 ",new Object[]{agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName(),chongZhengResultMsg});
//					}
//					throw new RuntimeException(otherFreezeRecordResultMsg);
//				}
//			}
//			else{
//				log.info("代理商编号{} 代理商名称 {} 机具款冻结记账 记账 {}", new Object[]{agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName(),terminalFreezeRecordResultMsg});
//				//调冲正
//				Map<String,Object> chongZhengResult = chongZheng(agentShareDaySettle);
//				Boolean chongZhengResultStatus = (Boolean) chongZhengResult.get("status");
//				String chongZhengResultMsg = (String) chongZhengResult.get("msg");
//				if (chongZhengResultStatus) {
//					log.info("代理商编号{} 代理商名称 {} 其他冲正记账 成功",agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName());
//				}
//				else{
//					log.info("代理商编号{} 代理商名称 {} 其他冲正记账 ",new Object[]{agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName(),chongZhengResultMsg});
//				}
//				throw new RuntimeException(terminalFreezeRecordResultMsg);
//			}
//		}
//		else{
//			log.info("代理商编号{} 代理商名称 {} 代理商分润入账 记账 {}", new Object[]{agentShareDaySettle.getAgentName(),agentShareDaySettle.getAgentName(),enterAccountRecordResultMsg});
////			msg.put("status", false);
////			msg.put("msg", enterAccountRecordResultMsg);
////			return msg;
//			throw new RuntimeException(enterAccountRecordResultMsg);
//		}
//		agentShareDaySettle.setEnterAccountStatus(EnterAccountStatus.ENTERACCOUNTED.toString());
//		
//		AgentPreRecordTotal agentPreRecordTotal= agentPreRecordTotalService.findAgentPreRecordTotalByAgentNo(agentShareDaySettle.getAgentNo());
//		if (agentPreRecordTotal != null) {
//			BigDecimal tuiCostAmount = agentPreRecordTotal.getTuiCostAmount();
//			if (tuiCostAmount == null) tuiCostAmount = BigDecimal.ZERO;
//			
//			BigDecimal openBackAmount = agentPreRecordTotal.getOpenBackAmount();
//			if (openBackAmount == null) openBackAmount = BigDecimal.ZERO;
//			
//			BigDecimal rateDiffAmount = agentPreRecordTotal.getRateDiffAmount();
//			if (rateDiffAmount == null) rateDiffAmount = BigDecimal.ZERO;
//			
//			BigDecimal riskSubAmount = agentPreRecordTotal.getRiskSubAmount();
//			if (riskSubAmount == null) riskSubAmount = BigDecimal.ZERO;
//			
//			BigDecimal merMgAmount = agentPreRecordTotal.getMerMgAmount();
//			if (merMgAmount == null) merMgAmount = BigDecimal.ZERO;
//			
//			BigDecimal bailSubAmount = agentPreRecordTotal.getBailSubAmount();
//			if (bailSubAmount == null) bailSubAmount = BigDecimal.ZERO;
//			
//			BigDecimal otherAmount = agentPreRecordTotal.getOtherAmount();
//			if (otherAmount == null) otherAmount = BigDecimal.ZERO;
//			
//			BigDecimal terminalFreezeAmount = agentPreRecordTotal.getTerminalFreezeAmount();
//			if (terminalFreezeAmount == null) terminalFreezeAmount = BigDecimal.ZERO;
//			
//			BigDecimal otherFreezeAmount = agentPreRecordTotal.getOtherFreezeAmount();
//			if (otherFreezeAmount == null) otherFreezeAmount = BigDecimal.ZERO;
//			
//			BigDecimal asdTuiCostAmount = agentShareDaySettle.getTuiCostAmount();
//			if (asdTuiCostAmount == null) {
//				asdTuiCostAmount = BigDecimal.ZERO;
//			}
//			BigDecimal asdOpenBackAmount  = agentShareDaySettle.getOpenBackAmount();
//			if (asdOpenBackAmount == null) {
//				asdOpenBackAmount = BigDecimal.ZERO;
//			}		
//			BigDecimal asdRateDiffAmount = agentShareDaySettle.getRateDiffAmount();
//			if (asdRateDiffAmount ==null) {
//				asdRateDiffAmount = BigDecimal.ZERO;
//			}		
//			BigDecimal asdRiskSubAmount = agentShareDaySettle.getRiskSubAmount();
//			if (asdRiskSubAmount == null) {
//				asdRiskSubAmount = BigDecimal.ZERO;
//			}
//			BigDecimal asdMerMgAmount = agentShareDaySettle.getMerMgAmount();
//			if (asdMerMgAmount == null) {
//				asdMerMgAmount = BigDecimal.ZERO;
//			}
//			BigDecimal asdBailSubAmount = agentShareDaySettle.getBailSubAmount();
//			if (asdBailSubAmount == null) {
//				asdBailSubAmount = BigDecimal.ZERO;
//			}
//			BigDecimal asdOtherAmount = agentShareDaySettle.getOtherAmount();
//			if (asdOtherAmount == null) {
//				asdOtherAmount = BigDecimal.ZERO;
//			}
//			BigDecimal asdTerminalFreezeAmount = agentShareDaySettle.getTerminalFreezeAmount();
//			if (asdTerminalFreezeAmount == null) {
//				asdTerminalFreezeAmount = BigDecimal.ZERO;
//			}
//			BigDecimal asdOtherFreezeAmount = agentShareDaySettle.getOtherFreezeAmount();
//			if (asdOtherFreezeAmount == null) {
//				asdOtherFreezeAmount = BigDecimal.ZERO;
//			}
//			
//			agentPreRecordTotal.setTuiCostAmount(tuiCostAmount.subtract(asdTuiCostAmount));
//			agentPreRecordTotal.setOpenBackAmount(openBackAmount.subtract(asdOpenBackAmount));
//			agentPreRecordTotal.setRateDiffAmount(rateDiffAmount.subtract(asdRateDiffAmount));
//			agentPreRecordTotal.setRiskSubAmount(riskSubAmount.subtract(asdRiskSubAmount));
//			agentPreRecordTotal.setMerMgAmount(merMgAmount.subtract(asdMerMgAmount));
//			agentPreRecordTotal.setBailSubAmount(bailSubAmount.subtract(asdBailSubAmount));
//			agentPreRecordTotal.setOtherAmount(otherAmount.subtract(asdOtherAmount));
//			agentPreRecordTotal.setTerminalFreezeAmount(terminalFreezeAmount.subtract(asdTerminalFreezeAmount));
//			agentPreRecordTotal.setOtherFreezeAmount(otherFreezeAmount.subtract(asdOtherFreezeAmount));
//		}
//		this.updateAgentShareDaySettle(agentShareDaySettle);
//		agentPreRecordTotalService.updateAgentPreRecordTotal(agentPreRecordTotal);
//		
//		
//		msg.put("status", true);
//		msg.put("msg", "代理商分润入账成功");
//		return msg;
//	}

	@Override
	public List<AgentShareDaySettle> exportAgentShareDaySettleList(AgentShareDaySettle agentShareDaySettle) throws Exception {
		
		if (StringUtils.isNotBlank(agentShareDaySettle.getTransDate1())) {
			agentShareDaySettle.setTransDate1(agentShareDaySettle.getTransDate1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentShareDaySettle.getTransDate2())) {
			agentShareDaySettle.setTransDate2(agentShareDaySettle.getTransDate2() + " 23:59:59");
		}
		if (StringUtils.isNotBlank(agentShareDaySettle.getGroupTime1())) {
			agentShareDaySettle.setGroupTime1(agentShareDaySettle.getGroupTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentShareDaySettle.getGroupTime2())) {
			agentShareDaySettle.setGroupTime2(agentShareDaySettle.getGroupTime2() + " 23:59:59");
		}
		return agentShareDaySettleMapper.exportAgentShareDaySettleList(agentShareDaySettle);
	}
	@Override
	public Map<String, Object> findCollectionGropByNoCollectAgent(Map<String, String> params) {
		return agentShareDaySettleMapper.findCollectionGropByNoCollectAgent(params);
	}
	@Override
	@Transactional
	public Map<String, Object> updateAgentShareDaySettleSplitBatch(List<AgentShareDaySettle> agentShareDaySettleList)
			throws Exception {
		Map<String,Object> msg=new HashMap<>();
		int i = 0;
		int batchCount = 200;
		List<AgentShareDaySettle> asdList = new ArrayList<>();
		List<List<?>> agentShareDaySettleSplitList = ListUtil.batchList(agentShareDaySettleList, batchCount);
		for (List<?> clist : agentShareDaySettleSplitList) {
			for (Object object : clist) {
				AgentShareDaySettle asd = (AgentShareDaySettle) object;
				if (asd.getMerCashFee() == null) {
					asd.setMerCashFee(BigDecimal.ZERO);
				}
				if (asd.getDaiCost() == null) {
					asd.setDaiCost(BigDecimal.ZERO);
				}
				if (asd.getDianCost() == null) {
					asd.setDianCost(BigDecimal.ZERO);
				}
				if (asd.getAcqOutProfit() == null) {
					asd.setAcqOutProfit(BigDecimal.ZERO);
				}
				if (asd.getPreTransShareAmount() == null) {
					asd.setPreTransShareAmount(BigDecimal.ZERO);
				}
				if (asd.getPreTransCashAmount() == null) {
					asd.setPreTransCashAmount(BigDecimal.ZERO);
				}
				if (asd.getOpenBackAmount() == null) {
					asd.setOpenBackAmount(BigDecimal.ZERO);
				}
				if (asd.getRateDiffAmount() == null) {
					asd.setRateDiffAmount(BigDecimal.ZERO);
				}
				if (asd.getRiskSubAmount() == null) {
					asd.setRiskSubAmount(BigDecimal.ZERO);
				}
				if (asd.getBailSubAmount() == null) {
					asd.setBailSubAmount(BigDecimal.ZERO);
				}
				if (asd.getMerMgAmount() == null) {
					asd.setMerMgAmount(BigDecimal.ZERO);
				}
				if (asd.getOtherAmount() == null) {
					asd.setOtherAmount(BigDecimal.ZERO);
				}
				if (asd.getAdjustTransShareAmount() == null) {
					asd.setAdjustTransShareAmount(BigDecimal.ZERO);
				}
				if (asd.getAdjustTransCashAmount() == null) {
					asd.setAdjustTransCashAmount(BigDecimal.ZERO);
				}
				if (asd.getAdjustTotalShareAmount() == null) {
					asd.setAdjustTotalShareAmount(BigDecimal.ZERO);
				}
				if (asd.getTerminalFreezeAmount() == null) {
					asd.setTerminalFreezeAmount(BigDecimal.ZERO);
				}
				if (asd.getOtherFreezeAmount() == null) {
					asd.setOtherFreezeAmount(BigDecimal.ZERO);
				}
				asdList.add(asd);
			}
			if (asdList.size() > 0) {
				log.info("修改代理商分润日结表{}条",asdList.size());
				int j = agentShareDaySettleMapper.updateAgentShareDaySettleBatch(asdList);
				if (j > 0) {
					i = i + j;
				}
				if (!asdList.isEmpty()) {
					asdList.clear();
				}
			}
		}
		if (i > 0) {
			msg.put("status", true);
			msg.put("msg", "执行成功");
		}
		else{
			msg.put("status", true);
			msg.put("msg", "没有修改任何数据");
		}
		return msg;
	}
	@Override
	public Map<String, Object> agentProfitSingleEnterAccount(AgentShareDaySettle agentDaySettle) throws JsonMappingException, IOException, Exception {
		Map<String,Object> msg=new HashMap<>();
		String agentNo = agentDaySettle.getAgentNo();
		
		AgentShareDaySettle asds= agentShareDaySettleMapper.findSingleNoEnterEntityById(agentDaySettle.getId());
		if (asds == null) {
			msg.put("status", false);
			msg.put("msg", "没有需要入账数据");
			return msg;
		}

		Map<String,Object> chilrenResult = agentProfitEnterAccount2(asds);
		Boolean chilrenResultStatus = (Boolean) chilrenResult.get("status");
		String chilrenResultMsg = (String) chilrenResult.get("msg");

		if (chilrenResultStatus) {
			msg.put("status", true);
			msg.put("msg", chilrenResultMsg);
			log.info(msg.toString());
			return msg;
		}
		msg.put("status", false);
		msg.put("msg", "入账失败");
		log.info(msg.toString());
		return msg;
	}
	@Override
	public Map<String, Object> findAgentShareDaySettleListCollection(AgentShareDaySettle agentShareDaySettle) {

		if (StringUtils.isNotBlank(agentShareDaySettle.getTransDate1())) {
			agentShareDaySettle.setTransDate1(agentShareDaySettle.getTransDate1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentShareDaySettle.getTransDate2())) {
			agentShareDaySettle.setTransDate2(agentShareDaySettle.getTransDate2() + " 23:59:59");
		}
		if (StringUtils.isNotBlank(agentShareDaySettle.getGroupTime1())) {
			agentShareDaySettle.setGroupTime1(agentShareDaySettle.getGroupTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentShareDaySettle.getGroupTime2())) {
			agentShareDaySettle.setGroupTime2(agentShareDaySettle.getGroupTime2() + " 23:59:59");
		}

		return agentShareDaySettleMapper.findAgentShareDaySettleListCollection(agentShareDaySettle);
	}
	
	@Override
	public AgentShareDaySettle findNoCollectTransShortInfoByAgentNodeAndLevel(Map<String, String> params) {
		return agentShareDaySettleMapper.findNoCollectTransShortInfoByAgentNodeAndLevel(params);
	}
	@Override
	public AgentShareDaySettle findEntityByBatchNoAndAgentNo(String batchNo, String agentNo) {
		return agentShareDaySettleMapper.findEntityByBatchNoAndAgentNo(batchNo, agentNo);
	}
	@Override
	public Map<String, Object> findNoCollectTransShortInfoGroupByAgentNodeAndLevel(Map<String, String> params) {
		return agentShareDaySettleMapper.findNoCollectTransShortInfoGroupByAgentNodeAndLevel(params);
	}

	@Override
	public void clearUnfreezeAmount(String agentNo) throws Exception {
		//获取分润账户
		String activityAccountType = "A";
		String activityUserId = agentNo;
		String activityAccountOwner = "000001";
		String activitySubjectNo = "224105";
		String activityCurrencyNo = "1";
		ExtAccountInfo activityExtAccountInfo= extAccountService.findExtAccountInfoByManyParams(activityAccountType, activityUserId, activityAccountOwner, activitySubjectNo, activityCurrencyNo);
		if(activityExtAccountInfo != null){
			String activityAccountNo = activityExtAccountInfo.getAccountNo();
			ExtAccount activityExtAccount= extAccountService.getExtAccount(activityAccountNo);		//获取获取活动补贴外部用户账户
			if(activityExtAccount == null){
				String.format("没找到外部账号,科目%s代理商编号%s", activitySubjectNo,activityUserId);
				log.error("清空代理商分润日结冻结金额失败，没找到外部账号,科目{}代理商编号{}",activitySubjectNo,activityUserId);
				return ;
			}
			BigDecimal preFreezeAmount = activityExtAccount.getPreFreezeAmount();
			log.info("账户："+activityAccountNo+"  冻结金额："+preFreezeAmount == null ? "null" : preFreezeAmount.toString());
			if(preFreezeAmount.compareTo(BigDecimal.ZERO) == 0){		//分润账户冻结金额被全部解冻
				agentShareDaySettleMapper.clearUnfreezeAmount(agentNo);
				log.info("清空代理商分润日结冻结金额成功，代理商编号：{}",agentNo);
			}
		}else{
			log.error("未找到代理商：{}分润账户,解冻分润日结冻结金额失败!",agentNo);
		}

	}

	@Override
	public AgentShareDaySettle findEntityById(Integer id) {
		return agentShareDaySettleMapper.findEntityById(id);
	}


}
