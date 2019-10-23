package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentUnfreeze;

public interface AgentUnfreezeService {
	int insertAgentUnfreeze(AgentUnfreeze agentUnfreeze);
	/**
	 * 代理商单个解冻
	 * @param agentUnfreeze
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> saveAgentsProfitUnfreeze(AgentUnfreeze agentUnfreeze) throws Exception;
	/**
	 * 代理商批量解冻操作
	 * @param agentUnfreezeList
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> saveAgentsProfitBatchUnfreeze(List<AgentUnfreeze> agentUnfreezeList) throws Exception;
	
	int updateAgentUnfreeze(AgentUnfreeze agentUnfreeze);
	int deleteAgentUnfreeze(Integer id);
	List<AgentUnfreeze> findAllAgentUnfreeze();
	List<AgentUnfreeze> findAgentUnfreezeList(AgentUnfreeze agentUnfreeze,Map<String, String> params,Sort sort,Page<AgentUnfreeze> page);
	AgentUnfreeze findAgentUnfreezeById(Integer id);
	List<AgentUnfreeze> exportAgentsProfitUnfreezeList(AgentUnfreeze agentUnfreeze,Map<String, String> params);
	
	
	Map<String, Object> saveBatchUnfreezeExcelDetails(Map<String, Object> map) throws Exception;
//	Map<String, Object> agentFreezeRecord(AgentShareDaySettle agentShareDaySettle,BigDecimal amount) ;
}
