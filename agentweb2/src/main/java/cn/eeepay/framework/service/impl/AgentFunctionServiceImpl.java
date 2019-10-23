package cn.eeepay.framework.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.AgentFunctionDao;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentFunctionBean;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.service.AgentFunctionService;

/**
 * Created by 666666 on 2018/3/7.
 */
@Service
public class AgentFunctionServiceImpl implements AgentFunctionService {
    @Resource
    private AgentFunctionDao agentFunctionDao;
    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<AgentFunctionBean> listAgentFunction(String loginAgentNo,String loginAgentType) {
    	String functionNumber = "032";
    	Map<String, Object> map = agentInfoDao.selectFunctionManage(functionNumber);
//    	if ("0".equals(map.get("function_switch").toString())) {
//		}
    	if((("1".equals(map.get("function_switch").toString()) && "1".equals(map.get("agent_control").toString())
    			&& agentFunctionDao.countAgentFunctionManage(loginAgentNo, functionNumber))
    			|| ("1".equals(map.get("function_switch").toString()) && "0".equals(map.get("agent_control").toString())))
    			&& (!"11".equals(loginAgentType))){//才显示全部
			return agentFunctionDao.listAgentFunction(loginAgentNo);
		}else{//去掉002,003 即不显示 允许二级代理商解绑,绑定机具 控制开关
			return agentFunctionDao.selectAgentFunction(loginAgentNo);
		}
    }

    @Override
    public Map<String,Object> selectFunctionManage(String functionNumber) {
		return agentInfoDao.selectFunctionManage(functionNumber);
	}
    
    @Override
    public boolean switchAgentFunction(AgentInfo loginAgent, String functionNumber, String functionValue) {
        AgentFunctionBean agentFunctionBean = agentFunctionDao.queryFunctionById(functionNumber);
        if (agentFunctionBean == null){
            throw new AgentWebException(String.format("功能编号为%s的记录不存在", functionNumber));
        }
        if (StringUtils.equals(functionValue, "1")){
            agentFunctionDao.insertAgentFunctionConfig(loginAgent.getAgentNo(), loginAgent.getOneLevelId(), functionNumber);
        }else{
            agentFunctionDao.deleteAgentFunction(Arrays.asList(loginAgent.getAgentNo()), functionNumber);
        }
        return true;
    }

    @Override
    public boolean insertSubAgentFunctionConfig(AgentInfo loginAgent, String subAgentNo, String functionNumber) {
        AgentInfo subAgent = agentInfoDao.selectByAgentNo(subAgentNo);
        if (subAgent == null || !StringUtils.equals(subAgent.getParentId(), loginAgent.getAgentNo())){
            throw new AgentWebException("代理商不存在,或者不是登陆代理商的二级代理商");
        }
        AgentFunctionBean agentFunctionBean = agentFunctionDao.queryFunctionById(functionNumber);
        if (agentFunctionBean == null){
            throw new AgentWebException("功能编号为的记录不存在");
        }
        if (agentFunctionDao.countAgentFunction(subAgentNo, functionNumber)){
            throw new AgentWebException("该代理商已存在");
        }
        agentFunctionDao.insertAgentFunctionConfig(subAgentNo, subAgent.getOneLevelId(), functionNumber);
        return true;
    }

    @Override
    public boolean deleteSubAgentFunctionConfig(AgentInfo loginAgent, List<String> subAgentNo, String functionNumber) {
        if (subAgentNo == null || subAgentNo.isEmpty()){
            return true;
        }
        AgentFunctionBean agentFunctionBean = agentFunctionDao.queryFunctionById(functionNumber);
        if (agentFunctionBean == null){
            throw new AgentWebException(String.format("功能编号为%s的记录不存在", functionNumber));
        }
        for (String agentNo : subAgentNo){
            if (!agentInfoDao.isDirectSubordinate(loginAgent.getAgentNo(), agentNo)){
                throw new AgentWebException("只能删除直接下级代理商的功能管控配置.");
            }
        }
        agentFunctionDao.deleteAgentFunction(subAgentNo, functionNumber);
        return true;
    }

    @Override
    public boolean queryFunctionValue(AgentInfo loginAgent, String functionNumber) {
        if (loginAgent.getAgentLevel() == 1){       // 一级代理商的功能管控都是打开的
            return true;
        }else if(loginAgent.getAgentLevel() >= 3){ // 三级以及三级一下的功能管控都是关闭的
            return false;
        }else{                                      // 二级代理商功能管控需要特殊处理
            // 一级必须有开启功能管控开关,且登陆代理商已经被一级代理商添加到agent_function_config才返回true
            return agentFunctionDao.countAgentFunction(loginAgent.getOneLevelId(), functionNumber) &&
                    agentFunctionDao.countAgentFunction(loginAgent.getAgentNo(), functionNumber);
        }
    }

    @Override
    public List<AgentInfo> listAgentFunctionConfig(AgentInfo loginAgent, String functionNumber, AgentInfo agentInfo, Page<AgentInfo> page) {
        return agentFunctionDao.listAgentFunctionConfig(loginAgent.getAgentNo(), functionNumber, agentInfo, page);
    }

    @Override
    public AgentInfo findAgentInfoByAgentNo(AgentInfo loginAgent, String agentNo) {
        AgentInfo agentInfoByAgentNo = agentFunctionDao.findAgentInfoByAgentNo(loginAgent.getAgentNo(), agentNo);
        if (agentInfoByAgentNo == null){
            throw new AgentWebException(String.format("代理商%s不存在,或者不是登陆代理商的二级代理商", agentNo));
        }
        return agentInfoByAgentNo;

    }

	@Override
	public boolean countAgentFunction(String entityId, String functionNumber) {
		return agentFunctionDao.countAgentFunction(entityId, functionNumber);
	}

	@Override
	public boolean countAgentFunctionManage(String entityId, String functionNumber) {
		return agentFunctionDao.countAgentFunctionManage(entityId, functionNumber);
	}
    
}
