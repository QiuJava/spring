package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentPreFreeze;

public interface AgentPreFreezeService {
	int insertAgentPreFreezeAndUpdateAccount(AgentPreFreeze agentPreFreeze) throws Exception;
	Map<String,Object> saveAgentsProfitPreFreeze(AgentPreFreeze agentPreFreeze,String freezeReason) throws Exception;
	Map<String, Object> saveAgentsProfitBatchPreFreeze(List<AgentPreFreeze> agentPreFreezeList) throws Exception;
	int insertAgentPreFreeze(AgentPreFreeze agentPreFreeze) throws Exception;
	int insertAgentPreFreezeBatch(List<AgentPreFreeze> list) throws Exception;
	Map<String,Object> insertAgentPreFreezeSplitBatch(List<AgentPreFreeze> agentPreFreezeList) throws Exception;
	int updateAgentPreFreeze(AgentPreFreeze agentPreFreeze) throws Exception;
	int deleteAgentPreFreeze(Integer id) throws Exception;
	List<AgentPreFreeze> findAllAgentPreFreeze() throws Exception;
	List<AgentPreFreeze> findAgentPreFreezeList(AgentPreFreeze agentPreFreeze,Sort sort,Page<AgentPreFreeze> page) throws Exception;
	AgentPreFreeze findAgentPreFreezeById(Integer id) throws Exception;
	List<cn.eeepay.framework.model.bill.AgentPreFreeze> exportAgentsProfitPreFreezeList(AgentPreFreeze agentPreFreeze);
}
