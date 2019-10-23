package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentFunctionBean;
import cn.eeepay.framework.model.AgentInfo;

/**
 * Created by 666666 on 2018/3/7.
 */
public interface AgentFunctionService {

    /**
     * 列举代理商系统功能管控规则列表
     * @return
     */
    List<AgentFunctionBean> listAgentFunction(String loginOneAgentNo,String loginAgentType);

    /**
     * 一级代理商开启/关闭功能管控
     * @param loginAgent 登陆的代理商
     * @param functionNumber   功能编号
     * @param functionValue   功能状态
     * @return
     */
    boolean switchAgentFunction(AgentInfo loginAgent, String functionNumber, String functionValue);

    /**
     * 一级代理商添加二级代理商功能配置
     * @param loginAgent 登陆的代理商
     * @param subAgentNo   下级代理商
     * @param functionNumber   功能编号
     * @return
     */
    boolean insertSubAgentFunctionConfig(AgentInfo loginAgent, String subAgentNo, String functionNumber);

    /**
     * 一级代理商删除二级代理商功能配置
     * @param loginAgent 登陆的代理商
     * @param subAgentNo   下级代理商
     * @param functionNumber   功能编号
     * @return
     */
    boolean deleteSubAgentFunctionConfig(AgentInfo loginAgent, List<String> subAgentNo, String functionNumber);

    /**
     * 一级代理商删除二级代理商功能配置
     * @param loginAgent 登陆的代理商
     * @param functionNumber   功能编号
     * @return
     */
    boolean queryFunctionValue(AgentInfo loginAgent, String functionNumber);

    /**
     * 列举代理商系统功能管控规则配置列表
     */
    List<AgentInfo> listAgentFunctionConfig(AgentInfo loginAgent, String functionNumber, AgentInfo agentInfo, Page<AgentInfo> page);

    AgentInfo findAgentInfoByAgentNo(AgentInfo loginAgent,String agentNo);
    
    /**
     * 根据功能编号查询功能开关状态
     * @return
     */
    Map<String,Object> selectFunctionManage(String functionNumber);
    
    boolean countAgentFunction(String entityId,String functionNumber);

	boolean countAgentFunctionManage(String entityId, String string);
}
