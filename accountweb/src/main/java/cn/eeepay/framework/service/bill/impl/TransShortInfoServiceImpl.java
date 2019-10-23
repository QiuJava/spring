package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.hawtdispatch.AggregatingExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.TransShortInfoMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentShareDaySettle;
import cn.eeepay.framework.model.bill.TransShortInfo;
import cn.eeepay.framework.service.bill.TransShortInfoService;
import cn.eeepay.framework.util.ListUtil;



@Service("transShortInfoService")
@Transactional
public class TransShortInfoServiceImpl implements TransShortInfoService{
	
	private static final Logger log = LoggerFactory.getLogger(TransShortInfoServiceImpl.class);
	
	@Resource
	private TransShortInfoMapper transShortInfoMapper;
	@Autowired
	private AgentInfoService agentInfoService;

	@Override
	public int insertTransShortInfo(TransShortInfo transShortInfo) throws Exception {
		return transShortInfoMapper.insertTransShortInfo(transShortInfo);
	}

	@Override
	public int updateTransShortInfo(TransShortInfo transShortInfo) throws Exception {
		return transShortInfoMapper.updateTransShortInfo(transShortInfo);
	}

	@Override
	public int deleteTransShortInfo(String plateOrderNo) throws Exception {
		return transShortInfoMapper.deleteTransShortInfo(plateOrderNo);
	}

	@Override
	public List<TransShortInfo> findAllTransShortInfo() throws Exception {
		return transShortInfoMapper.findAllTransShortInfo();
	}

	@Override
	public TransShortInfo findTransShortInfoByPlateOrderNo(String plateOrderNo) throws Exception {
		return transShortInfoMapper.findTransShortInfoByPlateOrderNo(plateOrderNo);
	}

	@Override
	public List<TransShortInfo> findTransShortInfoList(TransShortInfo transShortInfo, Sort sort,
			Page<TransShortInfo> page) {
		
		if (StringUtils.isNotBlank(transShortInfo.getTransTime1())) {
			transShortInfo.setTransTime1(transShortInfo.getTransTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(transShortInfo.getTransTime2())) {
			transShortInfo.setTransTime2(transShortInfo.getTransTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(transShortInfo.getCollectionTime1())) {
			transShortInfo.setCollectionTime1(transShortInfo.getCollectionTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(transShortInfo.getCollectionTime2())) {
			transShortInfo.setCollectionTime2(transShortInfo.getCollectionTime2() + " 23:59:59");
		}
		String agentNo = transShortInfo.getAgentNo();
		String agentLevel = null;
		if(StringUtils.isNotBlank(agentNo) && !agentNo.equals("ALL")){
			AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
			transShortInfo.setAgentNode(agentInfo.getAgentNode());		//设置节点
			agentLevel = agentInfo.getAgentLevel()+"";
		}
		List<TransShortInfo> transShortInfoList= transShortInfoMapper.findTransShortInfoList(transShortInfo, sort, page);
		for (TransShortInfo tsi : transShortInfoList) {
			choiceLevel(tsi,agentLevel);
//			tsi.setAgentShareAmount(tsi.getProfits1());tsi.setCashAgentShareAmount(tsi.getSettleProfits1());
//			if (tsi.getAgentLevel().equals("1")) {tsi.setAgentShareAmount(tsi.getProfits1());tsi.setCashAgentShareAmount(tsi.getSettleProfits1());}
//			else if (tsi.getAgentLevel().equals("2")) {tsi.setAgentShareAmount(tsi.getProfits2());tsi.setCashAgentShareAmount(tsi.getSettleProfits2());}
//			else if (tsi.getAgentLevel().equals("3")) {tsi.setAgentShareAmount(tsi.getProfits3());tsi.setCashAgentShareAmount(tsi.getSettleProfits3());}
//			else if (tsi.getAgentLevel().equals("4")) {tsi.setAgentShareAmount(tsi.getProfits4());tsi.setCashAgentShareAmount(tsi.getSettleProfits4());}
//			else if (tsi.getAgentLevel().equals("5")) {tsi.setAgentShareAmount(tsi.getProfits5());tsi.setCashAgentShareAmount(tsi.getSettleProfits5());}
//			else if (tsi.getAgentLevel().equals("6")) {tsi.setAgentShareAmount(tsi.getProfits6());tsi.setCashAgentShareAmount(tsi.getSettleProfits6());}
//			else if (tsi.getAgentLevel().equals("7")) {tsi.setAgentShareAmount(tsi.getProfits7());tsi.setCashAgentShareAmount(tsi.getSettleProfits7());}
//			else if (tsi.getAgentLevel().equals("8")) {tsi.setAgentShareAmount(tsi.getProfits8());tsi.setCashAgentShareAmount(tsi.getSettleProfits8());}
//			else if (tsi.getAgentLevel().equals("9")) {tsi.setAgentShareAmount(tsi.getProfits9());tsi.setCashAgentShareAmount(tsi.getSettleProfits9());}
//			else if (tsi.getAgentLevel().equals("10")) {tsi.setAgentShareAmount(tsi.getProfits10());tsi.setCashAgentShareAmount(tsi.getSettleProfits10());}
//			else if (tsi.getAgentLevel().equals("11")) {tsi.setAgentShareAmount(tsi.getProfits11());tsi.setCashAgentShareAmount(tsi.getSettleProfits11());}
//			else if (tsi.getAgentLevel().equals("12")) {tsi.setAgentShareAmount(tsi.getProfits12());tsi.setCashAgentShareAmount(tsi.getSettleProfits12());}
//			else if (tsi.getAgentLevel().equals("13")) {tsi.setAgentShareAmount(tsi.getProfits13());tsi.setCashAgentShareAmount(tsi.getSettleProfits13());}
//			else if (tsi.getAgentLevel().equals("14")) {tsi.setAgentShareAmount(tsi.getProfits14());tsi.setCashAgentShareAmount(tsi.getSettleProfits14());}
//			else if (tsi.getAgentLevel().equals("15")) {tsi.setAgentShareAmount(tsi.getProfits15());tsi.setCashAgentShareAmount(tsi.getSettleProfits15());}
//			else if (tsi.getAgentLevel().equals("16")) {tsi.setAgentShareAmount(tsi.getProfits16());tsi.setCashAgentShareAmount(tsi.getSettleProfits16());}
//			else if (tsi.getAgentLevel().equals("17")) {tsi.setAgentShareAmount(tsi.getProfits17());tsi.setCashAgentShareAmount(tsi.getSettleProfits17());}
//			else if (tsi.getAgentLevel().equals("18")) {tsi.setAgentShareAmount(tsi.getProfits18());tsi.setCashAgentShareAmount(tsi.getSettleProfits18());}
//			else if (tsi.getAgentLevel().equals("19")) {tsi.setAgentShareAmount(tsi.getProfits19());tsi.setCashAgentShareAmount(tsi.getSettleProfits19());}
//			else if (tsi.getAgentLevel().equals("20")) {tsi.setAgentShareAmount(tsi.getProfits20());tsi.setCashAgentShareAmount(tsi.getSettleProfits20());}
		}
		return transShortInfoList;
	}

	@Override
	public List<TransShortInfo> findAllTransShortInfoByTransTime(String transDate1) {
		String date1 = transDate1+" 00:00:00";
		String date2 = transDate1+" 23:59:59";
		return transShortInfoMapper.findAllTransShortInfoByTransTime(date1,date2);
	}

	public int updateTransShortInfoByDate(Map<String,String> param){
		return transShortInfoMapper.updateTransShortInfoByDate(param);
	}

	@Override
	public Map<String, Object> updateTransShortInfoSplitBatch(List<TransShortInfo> transShortInfoList) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		int i = 0;
		int batchCount = 200;
		List<TransShortInfo> tsiList = new ArrayList<>();
		List<List<?>> transShortInfoSplitList = ListUtil.batchList(transShortInfoList, batchCount);
		for (List<?> clist : transShortInfoSplitList) {
			for (Object object : clist) {
				TransShortInfo asd = (TransShortInfo) object;
				tsiList.add(asd);
			}
			if (tsiList.size() > 0) {
				log.info("更新代理商分润日结表{}条",tsiList.size());
				int j = transShortInfoMapper.updateTransShortInfoBatchByPlateOrderNo(tsiList);
				if (j > 0) {
					i = i + j;
				}
				if (!tsiList.isEmpty()) {
					tsiList.clear();
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
	public List<TransShortInfo> exportAgentsProfitTransShortInfoList(TransShortInfo transShortInfo) {
		if (StringUtils.isNotBlank(transShortInfo.getTransTime1())) {
			transShortInfo.setTransTime1(transShortInfo.getTransTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(transShortInfo.getTransTime2())) {
			transShortInfo.setTransTime2(transShortInfo.getTransTime2() + " 23:59:59");
		}
		
		if (StringUtils.isNotBlank(transShortInfo.getCollectionTime1())) {
			transShortInfo.setCollectionTime1(transShortInfo.getCollectionTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(transShortInfo.getCollectionTime2())) {
			transShortInfo.setCollectionTime2(transShortInfo.getCollectionTime2() + " 23:59:59");
		}
		String agentNo = transShortInfo.getAgentNo();
		String agentLevel = null;
		if(StringUtils.isNotBlank(agentNo) && !agentNo.equals("ALL")){
			AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
			transShortInfo.setAgentNode(agentInfo.getAgentNode());		//设置节点
			agentLevel = agentInfo.getAgentLevel()+"";
		}
		List<TransShortInfo> transShortInfoList= transShortInfoMapper.exportAgentsProfitTransShortInfoList(transShortInfo);
		for (TransShortInfo tsi : transShortInfoList) {
			choiceLevel(tsi,agentLevel);
			tsi.setOneAgentShareAmount(tsi.getProfits1());tsi.setOneCashAgentShareAmount(tsi.getSettleProfits1());
			//			if (tsi.getAgentLevel() != null) {
//				if (tsi.getAgentLevel().equals("1")) {tsi.setAgentShareAmount(tsi.getProfits1());tsi.setCashAgentShareAmount(tsi.getSettleProfits1());}
//				else if (tsi.getAgentLevel().equals("2")) {tsi.setAgentShareAmount(tsi.getProfits2());tsi.setCashAgentShareAmount(tsi.getSettleProfits2());}
//				else if (tsi.getAgentLevel().equals("3")) {tsi.setAgentShareAmount(tsi.getProfits3());tsi.setCashAgentShareAmount(tsi.getSettleProfits3());}
//				else if (tsi.getAgentLevel().equals("4")) {tsi.setAgentShareAmount(tsi.getProfits4());tsi.setCashAgentShareAmount(tsi.getSettleProfits4());}
//				else if (tsi.getAgentLevel().equals("5")) {tsi.setAgentShareAmount(tsi.getProfits5());tsi.setCashAgentShareAmount(tsi.getSettleProfits5());}
//				else if (tsi.getAgentLevel().equals("6")) {tsi.setAgentShareAmount(tsi.getProfits6());tsi.setCashAgentShareAmount(tsi.getSettleProfits6());}
//				else if (tsi.getAgentLevel().equals("7")) {tsi.setAgentShareAmount(tsi.getProfits7());tsi.setCashAgentShareAmount(tsi.getSettleProfits7());}
//				else if (tsi.getAgentLevel().equals("8")) {tsi.setAgentShareAmount(tsi.getProfits8());tsi.setCashAgentShareAmount(tsi.getSettleProfits8());}
//				else if (tsi.getAgentLevel().equals("9")) {tsi.setAgentShareAmount(tsi.getProfits9());tsi.setCashAgentShareAmount(tsi.getSettleProfits9());}
//				else if (tsi.getAgentLevel().equals("10")) {tsi.setAgentShareAmount(tsi.getProfits10());tsi.setCashAgentShareAmount(tsi.getSettleProfits10());}
//				else if (tsi.getAgentLevel().equals("11")) {tsi.setAgentShareAmount(tsi.getProfits11());tsi.setCashAgentShareAmount(tsi.getSettleProfits11());}
//				else if (tsi.getAgentLevel().equals("12")) {tsi.setAgentShareAmount(tsi.getProfits12());tsi.setCashAgentShareAmount(tsi.getSettleProfits12());}
//				else if (tsi.getAgentLevel().equals("13")) {tsi.setAgentShareAmount(tsi.getProfits13());tsi.setCashAgentShareAmount(tsi.getSettleProfits13());}
//				else if (tsi.getAgentLevel().equals("14")) {tsi.setAgentShareAmount(tsi.getProfits14());tsi.setCashAgentShareAmount(tsi.getSettleProfits14());}
//				else if (tsi.getAgentLevel().equals("15")) {tsi.setAgentShareAmount(tsi.getProfits15());tsi.setCashAgentShareAmount(tsi.getSettleProfits15());}
//				else if (tsi.getAgentLevel().equals("16")) {tsi.setAgentShareAmount(tsi.getProfits16());tsi.setCashAgentShareAmount(tsi.getSettleProfits16());}
//				else if (tsi.getAgentLevel().equals("17")) {tsi.setAgentShareAmount(tsi.getProfits17());tsi.setCashAgentShareAmount(tsi.getSettleProfits17());}
//				else if (tsi.getAgentLevel().equals("18")) {tsi.setAgentShareAmount(tsi.getProfits18());tsi.setCashAgentShareAmount(tsi.getSettleProfits18());}
//				else if (tsi.getAgentLevel().equals("19")) {tsi.setAgentShareAmount(tsi.getProfits19());tsi.setCashAgentShareAmount(tsi.getSettleProfits19());}
//				else if (tsi.getAgentLevel().equals("20")) {tsi.setAgentShareAmount(tsi.getProfits20());tsi.setCashAgentShareAmount(tsi.getSettleProfits20());}
//			}
			if (tsi.getAgentShareAmount() == null) {
				tsi.setAgentShareAmount(BigDecimal.ZERO);
			}
			if (tsi.getCashAgentShareAmount() == null) {
				tsi.setCashAgentShareAmount(BigDecimal.ZERO);
			}
		}
		 return transShortInfoList;
	}

	@Override
	public List<TransShortInfo> findNoCollectTransShortInfo(Map<String, String> params) {
		return transShortInfoMapper.findNoCollectTransShortInfo(params);
	}


	public void choiceLevel(TransShortInfo tsi,String agentLevel){
		if(agentLevel==null){
			agentLevel = "";
		}
		switch (agentLevel){
			case "1":
				tsi.setAgentShareAmount(tsi.getProfits1());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits1());
				break;
			case "2":
				tsi.setAgentShareAmount(tsi.getProfits2());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits2());
				break;
			case "3":
				tsi.setAgentShareAmount(tsi.getProfits3());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits3());
				break;
			case "4":
				tsi.setAgentShareAmount(tsi.getProfits4());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits4());
				break;
			case "5":
				tsi.setAgentShareAmount(tsi.getProfits5());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits5());
				break;
			case "6":
				tsi.setAgentShareAmount(tsi.getProfits6());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits6());
				break;
			case "7":
				tsi.setAgentShareAmount(tsi.getProfits7());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits7());
				break;
			case "8":
				tsi.setAgentShareAmount(tsi.getProfits8());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits8());
				break;
			case "9":
				tsi.setAgentShareAmount(tsi.getProfits9());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits9());
				break;
			case "10":
				tsi.setAgentShareAmount(tsi.getProfits10());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits10());
				break;
			case "11":
				tsi.setAgentShareAmount(tsi.getProfits11());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits11());
				break;
			case "12":
				tsi.setAgentShareAmount(tsi.getProfits12());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits12());
				break;
			case "13":
				tsi.setAgentShareAmount(tsi.getProfits13());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits13());
				break;
			case "14":
				tsi.setAgentShareAmount(tsi.getProfits14());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits14());
				break;
			case "15":
				tsi.setAgentShareAmount(tsi.getProfits15());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits15());
				break;
			case "16":
				tsi.setAgentShareAmount(tsi.getProfits16());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits16());
				break;
			case "17":
				tsi.setAgentShareAmount(tsi.getProfits17());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits17());
				break;
			case "18":
				tsi.setAgentShareAmount(tsi.getProfits18());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits18());
				break;
			case "19":
				tsi.setAgentShareAmount(tsi.getProfits19());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits19());
				break;
			case "20":
				tsi.setAgentShareAmount(tsi.getProfits20());
				tsi.setCashAgentShareAmount(tsi.getSettleProfits20());
				break;
			default:
				tsi.setAgentShareAmount(tsi.getProfits1());tsi.setCashAgentShareAmount(tsi.getSettleProfits1());
		}
	}
        

}
