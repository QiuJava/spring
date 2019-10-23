package cn.eeepay.framework.service.nposp.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.AgentInfoMapper;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.service.nposp.AgentInfoService;

@Service("agentInfoService")
@Transactional("nposp")
public class AgentServiceImpl implements AgentInfoService {
	@Resource
	private AgentInfoMapper agentInfoMapper;
	@Override
	//@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public AgentInfo findAgentByUserId(String userId) {
		return agentInfoMapper.findAgentInfoByUserId(userId);
	}
	@Override
	public List<String> findAgentListByParams(String mobilephone , String userName) {
		return agentInfoMapper.findAgentListByParams(mobilephone,userName);
	}
	@Override
	public AgentInfo findEntityByAgentNo(String agentNo) {
		return agentInfoMapper.findEntityByAgentNo(agentNo);
	}
	@Override
	public List<AgentInfo> findSelectAgentInfo(AgentInfo agentInfo,Integer limit) {
		return agentInfoMapper.findSelectAgentInfo(agentInfo,limit);
	}
	@Override
	public List<AgentInfo> findAllAgentInfoList() {
		return agentInfoMapper.findAllAgentInfoList();
	}
	@Override
	public List<AgentInfo> findAllOneAgentInfoList() {
		return agentInfoMapper.findAllOneAgentInfoList();
	}
	@Override
	public List<String> findAgentListByAgentNo(String oneAgentNo) {
		return agentInfoMapper.findAgentListByAgentNo(oneAgentNo);
	}
	@Override
	public List<AgentInfo> findEntityByLevel(String level) {
		return agentInfoMapper.findEntityByLevel(level);
	}
	@Override
	public AgentInfo findEntityById(String id) {
		return agentInfoMapper.findEntityById(id);
	}
	@Override
	public List<AgentInfo> findOpenDirectEntityByParentAgentNo(String parentAgentNo) {
		return agentInfoMapper.findOpenDirectEntityByParentAgentNo(parentAgentNo);
	}
	@Override
	public List<AgentInfo> findEntityByLevelSwitch(Integer level) {
		return agentInfoMapper.findEntityByLevelSwitch(level);
	}

	@Override
	public List<String> findAgentList(AgentInfo agentInfo) {
		return agentInfoMapper.findAgentList(agentInfo);
	}
}
