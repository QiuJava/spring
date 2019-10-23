package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentAccPreAdjust;
import cn.eeepay.framework.model.bill.AgentPreRecordTotal;
import cn.eeepay.framework.service.bill.AgentPreRecordTotalService;
import cn.eeepay.framework.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("agentPreRecordTotalService")
@Transactional
public class AgentPreRecordTotalServiceImpl implements AgentPreRecordTotalService{
	
	private static final Logger log = LoggerFactory.getLogger(AgentPreRecordTotalServiceImpl.class);
	@Resource
	private AgentPreRecordTotalMapper agentPreRecordTotalMapper;

	@Override
	public List<AgentPreRecordTotal> exportAgentPreRecordTotalList(AgentPreRecordTotal agentPreRecordTotal, String userNoStrs,Sort sort) throws Exception {
		return agentPreRecordTotalMapper.exportAgentPreRecordTotalList(agentPreRecordTotal,userNoStrs,sort);
	}

	@Override
	public Map<String, Object> findAgentPreRecordTotalListCollection(AgentPreRecordTotal agentPreRecordTotal, String userNoStrs) throws Exception {
		return agentPreRecordTotalMapper.findAgentPreRecordTotalListCollection(agentPreRecordTotal,userNoStrs);
	}

	@Override
	public Map<String, Object> findAgentPreRecordTotalListCollectionByUserNoStrs(String userNoStrs) throws Exception {
		return agentPreRecordTotalMapper.findAgentPreRecordTotalListCollectionByUserNoStrs(userNoStrs);
	}

	@Override
	public Map<String, Object> findAgentPreRecordTotalByAgentNoAndSubjectNoCollection(String agentNo,String subjectNo) {
		return agentPreRecordTotalMapper.findAgentPreRecordTotalByAgentNoAndSubjectNoCollection(agentNo,subjectNo);
	}


	@Override
	public Map<String, Object> findAgentPreRecord(String accountNo) {
		return agentPreRecordTotalMapper.findAgentPreRecord(accountNo);
	}

	@Override
	public List<AgentPreRecordTotal> findAgentPreRecordTotalList(AgentPreRecordTotal agentPreRecordTotal,
			String userNoStrs, Sort sort, Page<AgentPreRecordTotal> page) throws Exception {
		return agentPreRecordTotalMapper.findAgentPreRecordTotalList(agentPreRecordTotal,userNoStrs,sort,page );
	}

	@Override
	public AgentPreRecordTotal findAgentPreRecordTotalByAgentNoAndSubjectNo(String agentNo, String subjectNo) throws Exception {
		return agentPreRecordTotalMapper.findAgentPreRecordTotalByAgentNoAndSubjectNo(agentNo,subjectNo);
	}

//	@Override
//	public AgentPreRecordTotal findAgentPreRecordTotalByAgentNoAndSubjectNo(AgentAccPreAdjust agentAccPreAdjust, String subjectNo) throws Exception {
//		return agentPreRecordTotalMapper.findAgentPreRecordTotalByAgentNoAndSubjectNo(agentAccPreAdjust,subjectNo);
//	}

	@Override
	public AgentPreRecordTotal findAgentPreRecordTotalByAgentNo(String agentNo) {
		AgentPreRecordTotal agentPreRecordTotal = agentPreRecordTotalMapper.findAgentPreRecordTotalByAgentNo(agentNo);
		if (agentPreRecordTotal != null) {
			BigDecimal openBackAmount = agentPreRecordTotal.getOpenBackAmount();
			if (openBackAmount == null) agentPreRecordTotal.setOpenBackAmount(BigDecimal.ZERO);
			
			BigDecimal rateDiffAmount = agentPreRecordTotal.getRateDiffAmount();
			if (rateDiffAmount == null)  agentPreRecordTotal.setRateDiffAmount(BigDecimal.ZERO);
			
			BigDecimal tuiCostAmount = agentPreRecordTotal.getTuiCostAmount();
			if (tuiCostAmount == null)  agentPreRecordTotal.setTuiCostAmount(BigDecimal.ZERO);
			
			BigDecimal riskSubAmount = agentPreRecordTotal.getRiskSubAmount();
			if (riskSubAmount == null)  agentPreRecordTotal.setRiskSubAmount(BigDecimal.ZERO);
			
			BigDecimal merMgAmount = agentPreRecordTotal.getMerMgAmount();
			if (merMgAmount == null)  agentPreRecordTotal.setMerMgAmount(BigDecimal.ZERO);
			
			BigDecimal bailSubAmount = agentPreRecordTotal.getBailSubAmount();
			if (bailSubAmount == null)  agentPreRecordTotal.setBailSubAmount(BigDecimal.ZERO);
			
			BigDecimal otherAmount = agentPreRecordTotal.getOtherAmount();
			if (otherAmount == null)  agentPreRecordTotal.setOtherAmount(BigDecimal.ZERO);
			
			BigDecimal terminalFreezeAmount = agentPreRecordTotal.getTerminalFreezeAmount();
			if (terminalFreezeAmount == null)  agentPreRecordTotal.setTerminalFreezeAmount(BigDecimal.ZERO);
			
			BigDecimal otherFreezeAmount = agentPreRecordTotal.getOtherFreezeAmount();
			if (otherFreezeAmount == null)  agentPreRecordTotal.setOtherFreezeAmount(BigDecimal.ZERO);
		}
		return agentPreRecordTotal;
	}
	@Override
	public List<AgentPreRecordTotal> findAgentPreRecordTotalByAgentNo(List<String> agentNos) {
		return agentPreRecordTotalMapper.findAgentPreRecordTotalByAgentNos(agentNos);
	}
	@Override
	public int updateAgentPreRecordTotalBatch(List<AgentPreRecordTotal> list) throws Exception {
		return agentPreRecordTotalMapper.updateAgentPreRecordTotalBatch(list);
	}
	@Override
	public Map<String, Object> updateAgentPreRecordTotalSplitBatch(List<AgentPreRecordTotal> agentPreRecordTotalList)
			throws Exception {
		Map<String,Object> msg=new HashMap<>();
		int i = 0;
		int batchCount = 200;
		List<AgentPreRecordTotal> asdList = new ArrayList<>();
		List<List<?>> agentPreRecordTotalSplitList = ListUtil.batchList(agentPreRecordTotalList, batchCount);
		for (List<?> clist : agentPreRecordTotalSplitList) {
			for (Object object : clist) {
				AgentPreRecordTotal asd = (AgentPreRecordTotal) object;
				if (asd.getOpenBackAmount() == null) {
					asd.setOpenBackAmount(BigDecimal.ZERO);
				}
				if (asd.getRateDiffAmount() == null) {
					asd.setRateDiffAmount(BigDecimal.ZERO);
				}
				if (asd.getTuiCostAmount() == null) {
					asd.setTuiCostAmount(BigDecimal.ZERO);
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
				if (asd.getTerminalFreezeAmount() == null) {
					asd.setTerminalFreezeAmount(BigDecimal.ZERO);
				}
				if (asd.getOtherFreezeAmount() == null) {
					asd.setOtherFreezeAmount(BigDecimal.ZERO);
				}
				asdList.add(asd);
			}
			if (asdList.size() > 0) {
				log.info("代理商预记账累计表{}条",asdList.size());
				int j = agentPreRecordTotalMapper.updateAgentPreRecordTotalBatch(asdList);
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
	public int updateAgentPreRecordTotal(AgentPreRecordTotal agentPreRecordTotal) throws Exception {
		return agentPreRecordTotalMapper.updateAgentPreRecordTotal(agentPreRecordTotal);
	}
	@Override
	public int insertAgentPreRecordTotal(AgentPreRecordTotal agentPreRecordTotal) {
		if (agentPreRecordTotal.getOpenBackAmount() == null) {
			agentPreRecordTotal.setOpenBackAmount(BigDecimal.ZERO);
		}
		if (agentPreRecordTotal.getRateDiffAmount() == null) {
			agentPreRecordTotal.setRateDiffAmount(BigDecimal.ZERO);
		}
		if (agentPreRecordTotal.getTuiCostAmount() == null) {
			agentPreRecordTotal.setTuiCostAmount(BigDecimal.ZERO);
		}
		if (agentPreRecordTotal.getRiskSubAmount() == null) {
			agentPreRecordTotal.setRiskSubAmount(BigDecimal.ZERO);
		}
		if (agentPreRecordTotal.getMerMgAmount() == null) {
			agentPreRecordTotal.setMerMgAmount(BigDecimal.ZERO);
		}
		if (agentPreRecordTotal.getOtherAmount() == null) {
			agentPreRecordTotal.setOtherAmount(BigDecimal.ZERO);
		}
		if (agentPreRecordTotal.getTerminalFreezeAmount() == null) {
			agentPreRecordTotal.setTerminalFreezeAmount(BigDecimal.ZERO);
		}
		if (agentPreRecordTotal.getOtherFreezeAmount() == null) {
			agentPreRecordTotal.setOtherFreezeAmount(BigDecimal.ZERO);
		}
		if (agentPreRecordTotal.getBailSubAmount() == null) {
			agentPreRecordTotal.setBailSubAmount(BigDecimal.ZERO);
		}
		return agentPreRecordTotalMapper.insertAgentPreRecordTotal(agentPreRecordTotal);
	}
	@Override
	public Map<String, Object> insertAgentPreRecordTotalSplitBatch(List<AgentPreRecordTotal> agentPreRecordTotalList)
			throws Exception {
		Map<String,Object> msg=new HashMap<>();
	    int i = 0;
	    int batchCount = 200;
	    List<AgentPreRecordTotal> asdList = new ArrayList<>();
	    List<List<?>> agentPreRecordTotalSplitList = ListUtil.batchList(agentPreRecordTotalList, batchCount);
	    for (List<?> clist : agentPreRecordTotalSplitList) {
	      for (Object object : clist) {
	    	AgentPreRecordTotal asd = (AgentPreRecordTotal) object;
	    	if (asd.getOpenBackAmount() == null) {
	    		  asd.setOpenBackAmount(BigDecimal.ZERO);
	  		}
	  		if (asd.getRateDiffAmount() == null) {
	  			asd.setRateDiffAmount(BigDecimal.ZERO);
	  		}
	  		if (asd.getTuiCostAmount() == null) {
	  			asd.setTuiCostAmount(BigDecimal.ZERO);
	  		}
	  		if (asd.getRiskSubAmount() == null) {
	  			asd.setRiskSubAmount(BigDecimal.ZERO);
	  		}
	  		if (asd.getMerMgAmount() == null) {
	  			asd.setMerMgAmount(BigDecimal.ZERO);
	  		}
	  		if (asd.getOtherAmount() == null) {
	  			asd.setOtherAmount(BigDecimal.ZERO);
	  		}
	  		if (asd.getTerminalFreezeAmount() == null) {
	  			asd.setTerminalFreezeAmount(BigDecimal.ZERO);
	  		}
	  		if (asd.getOtherFreezeAmount() == null) {
	  			asd.setOtherFreezeAmount(BigDecimal.ZERO);
	  		}
	  		if (asd.getBailSubAmount() == null) {
	  			asd.setBailSubAmount(BigDecimal.ZERO);
	  		}
	        asdList.add(asd);
	      }
	      if (asdList.size() > 0) {
	        log.info("代理商预记账累计表{}条",asdList.size());
	        int j = agentPreRecordTotalMapper.insertAgentPreRecordTotalBatch(asdList);
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




}
