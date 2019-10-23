package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentPreAdjust;
import cn.eeepay.framework.model.nposp.AgentInfo;

import java.util.List;
import java.util.Map;

public interface AgentPreAdjustService {

	List<AgentPreAdjust> findAgentsProfitPreAdjustList(AgentPreAdjust agentPreAdjust, Map<String, Object> param, Sort sort, Page<AgentPreAdjust> page);

	int insertAgentPreAdjustAndUpdateAccount(AgentPreAdjust agentPreAdjust,AgentInfo agentInfo) throws Exception;

	List<AgentPreAdjust> exportAgentsProfitPreAdjustList(AgentPreAdjust agentPreAdjust);
	
	int insertAgentPreAdjust(AgentPreAdjust agentPreAdjust);
	
	Map<String, Object> insertAgentPreAdjustAndUpdateAccountExcel(List<AgentPreAdjust> agentPreAdjustsExcel) throws Exception;


}
