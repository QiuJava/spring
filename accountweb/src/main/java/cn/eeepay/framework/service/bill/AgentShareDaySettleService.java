package cn.eeepay.framework.service.bill;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonMappingException;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentShareDaySettle;

public interface AgentShareDaySettleService {
	
	AgentShareDaySettle findEntityById(Integer id);
	
	int insertAgentShareDaySettle(AgentShareDaySettle agentShareDaySettle);
	
	Map<String,Object> insertAgentShareDaySettleSplitBatch(List<AgentShareDaySettle> agentShareDaySettleList) throws Exception;
	
	int updateAgentShareDaySettle(AgentShareDaySettle agentShareDaySettle);
	
	Map<String,Object> updateAgentShareDaySettleSplitBatch(List<AgentShareDaySettle> agentShareDaySettleList) throws Exception;
	
	int deleteAgentShareDaySettle(Integer id);
	
	List<AgentShareDaySettle> findAllAgentShareDaySettle();
	
	List<AgentShareDaySettle> findAgentShareDaySettleList(AgentShareDaySettle agentShareDaySettle,Sort sort,Page<AgentShareDaySettle> page);
	
	Map<String,Object> findAgentShareDaySettleListCollection(AgentShareDaySettle agentShareDaySettle);
	
	AgentShareDaySettle findAgentShareDaySettleById(Integer id);
	
	
	/**
	 * 
	 * @param transDate1
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> validateAgentProfitCollection(String transDate1) throws Exception;
	
	/**
	 * 代理商分润汇总
	 * @param transDate1
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> agentProfitCollection(String transDate1,String operater) throws Exception;
	/**
	 * 代理商分润试算
	 * @param transDate1
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> agentProfitTryCalculation(String transDate1) throws Exception;
	
	/**
	 * 代理商分润入账
	 * @param transDate1
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> agentProfitEnterAccount(String transDate1) throws Exception;
	
	List<AgentShareDaySettle> findCollectionGropByAgent(String transDate1) throws Exception;

	List<AgentShareDaySettle> exportAgentShareDaySettleList(AgentShareDaySettle agentShareDaySettle)throws Exception;
	
	Map<String,Object> findCollectionGropByNoCollectAgent(Map<String, String> params);

	Map<String, Object> agentProfitSingleEnterAccount(AgentShareDaySettle agentShareDaySettle) throws JsonMappingException, IOException, Exception;
	

	AgentShareDaySettle findNoCollectTransShortInfoByAgentNodeAndLevel(Map<String, String> params);
	
	AgentShareDaySettle findEntityByBatchNoAndAgentNo(String batchNo,String agentNo);
	
	Map<String,Object> findNoCollectTransShortInfoGroupByAgentNodeAndLevel(Map<String, String> params);
	
	public void clearUnfreezeAmount(String agentNo) throws Exception;
}
