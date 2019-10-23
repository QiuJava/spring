package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.AgentShareRule;
import cn.eeepay.framework.model.AgentShareRuleTask;

public interface AgentShareTaskService {

	/**
	 * 添加一条任务
	 * @param shareList
	 * @return
	 */
	int insertAgentShareList(AgentShareRuleTask shareList);

	/**
	 * 删除一条任务
	 * @param id
	 * @return
	 */
	int deleteAgentShareTask(Integer id);

	/**
	 * 查询任务列表
	 * @param shareId
	 * @return
	 */
	List<AgentShareRuleTask> getAgentShareRuleTask(Integer shareId);
	
	/**
	 * 用于交易详情查询
	 * @param agentNo
	 * @param serviceId
	 * @return
	 */
	AgentShareRule queryByAgentNoAndServiceId(String agentNo,String serviceId);


	/**
	 * 根据代理商编号查询分润列表
	 * @param param
	 * @return
	 */
	List<AgentShareRule> getAgentShareList(String param);
}
