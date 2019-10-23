package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentAccPreAdjust;
import cn.eeepay.framework.model.bill.AgentPreRecordTotal;

import java.util.List;
import java.util.Map;

public interface AgentPreRecordTotalService {
	
	int insertAgentPreRecordTotal(AgentPreRecordTotal agentPreRecordTotal);
	
	List<AgentPreRecordTotal> findAgentPreRecordTotalList(AgentPreRecordTotal agentPreRecordTotal, String userNoStrs, Sort sort,
			Page<AgentPreRecordTotal> page) throws Exception;

	AgentPreRecordTotal findAgentPreRecordTotalByAgentNoAndSubjectNo(String agentNo,String subjectNo) throws Exception;

//	AgentPreRecordTotal findAgentPreRecordTotalByAgentNoAndSubjectNo(AgentAccPreAdjust agentAccPreAdjust, String subjectNo) throws Exception;

	AgentPreRecordTotal findAgentPreRecordTotalByAgentNo(String agentNo) throws Exception;
	
	List<AgentPreRecordTotal> findAgentPreRecordTotalByAgentNo(List<String> agentNos) throws Exception;
	
	int updateAgentPreRecordTotalBatch(List<AgentPreRecordTotal> list) throws Exception;
	
	public Map<String, Object> updateAgentPreRecordTotalSplitBatch(List<AgentPreRecordTotal> agentPreRecordTotalList) throws Exception;
	
	int updateAgentPreRecordTotal(AgentPreRecordTotal agentPreRecordTotal) throws Exception;
	
	Map<String,Object> insertAgentPreRecordTotalSplitBatch(List<AgentPreRecordTotal> agentPreRecordTotalList) throws Exception;


	List<AgentPreRecordTotal> exportAgentPreRecordTotalList(AgentPreRecordTotal agentPreRecordTotal, String userNoStrs,Sort sort) throws Exception;

	Map<String,Object> findAgentPreRecordTotalListCollection(AgentPreRecordTotal agentPreRecordTotal, String userNoStrs) throws Exception;

	Map<String,Object> findAgentPreRecordTotalListCollectionByUserNoStrs(String userNoStrs) throws Exception;


    Map<String,Object> findAgentPreRecord(String accountNo);

	public Map<String, Object> findAgentPreRecordTotalByAgentNoAndSubjectNoCollection(String agentNo,String subjectNo);

}
