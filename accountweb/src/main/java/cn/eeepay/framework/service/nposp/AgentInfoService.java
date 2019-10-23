package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.model.nposp.AgentInfo;

import java.util.List;

public interface AgentInfoService {
	/**
	 * 查询代理商 根据 id
	 * @return
	 */
	public AgentInfo findAgentByUserId(String userId);
	
	/**
	 * 查询 代理商编号  通过  用户名、手机号
	 * @param userName
	 * @param mobilephone
	 * @return
	 */
	public List<String> findAgentListByParams(String userName ,String mobilephone);
	
	AgentInfo findEntityByAgentNo(String agentNo);

	public List<AgentInfo> findSelectAgentInfo(AgentInfo agentInfo,Integer limit);
	
	public List<AgentInfo> findAllAgentInfoList();

	public List<AgentInfo> findAllOneAgentInfoList();

	public List<String> findAgentListByAgentNo(String oneAgentNo);
	
	public List<AgentInfo> findEntityByLevel(String level);
	
	public AgentInfo findEntityById(String id);
	
	
	public List<AgentInfo> findOpenDirectEntityByParentAgentNo(String parentAgentNo);
	
	public List<AgentInfo> findEntityByLevelSwitch(Integer level);

	public List<String> findAgentList(AgentInfo agentInfo);
}
