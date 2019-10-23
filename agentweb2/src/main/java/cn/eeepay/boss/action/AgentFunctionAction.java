package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.service.AgentFunctionService;
import cn.eeepay.framework.service.AgentInfoService;

/**
 * Created by 666666 on 2018/3/7.
 */
@RestController
@RequestMapping("/agentFunction")
public class AgentFunctionAction {

    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private AgentFunctionService agentFunctionService;

    @RequestMapping("/listAgentFunctionRule")
    public ResponseBean listAgentFunctionRule(){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            // 只有一级代理商才能查到数据
            if (loginAgent.getAgentLevel() != 1){
                return new ResponseBean("只有一级代理商才能查询");
            }
            return new ResponseBean(agentFunctionService.listAgentFunction(loginAgent.getAgentNo(),loginAgent.getAgentType()));
        }catch (Exception e){
            return new ResponseBean("查询功能管控列表异常");
        }
    }

    @RequestMapping("/listAgentFunctionConfig")
    public ResponseBean listAgentFunctionConfig(@RequestBody AgentInfo agentInfo, String functionNumber, int pageNo, int pageSize){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            // 只有一级代理商才能查到数据
            if (loginAgent.getAgentLevel() != 1){
                return new ResponseBean("只有一级代理商才能查询");
            }
            Page<AgentInfo> page = new Page<>(pageNo, pageSize);
            List<AgentInfo> agentInfos =  agentFunctionService.listAgentFunctionConfig(loginAgent, functionNumber, agentInfo, page);
            return new ResponseBean(agentInfos, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean("查询功能管控列表异常");
        }
    }

    @RequestMapping("/switchAgentFunction")
    @SystemLog(description = "一级代理商设置功能管控开关")
    public ResponseBean switchAgentFunction(String functionNumber, String functionValue){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            // 只有一级代理商才能该操作
            if (loginAgent.getAgentLevel() != 1){
                return new ResponseBean("只有一级代理商才能开启/关闭功能管控");
            }
            return new ResponseBean(agentFunctionService.switchAgentFunction(loginAgent, functionNumber, functionValue));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/insertSubAgentFunctionConfig")
    @SystemLog(description = "一级代理商功能管控添加二级代理商")
    public ResponseBean insertSubAgentFunctionConfig(String agentNo, String functionNumber){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            // 只有一级代理商才能该操作
            if (loginAgent.getAgentLevel() != 1){
                return new ResponseBean("只有一级代理商才能为下级添加功能管控配置");
            }
            return new ResponseBean(agentFunctionService.insertSubAgentFunctionConfig(loginAgent, agentNo, functionNumber));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/deleteSubAgentFunctionConfig")
    @SystemLog(description = "一级代理商功能管控删除二级代理商")
    public ResponseBean deleteSubAgentFunctionConfig(@RequestBody List<String> agentNo, @RequestParam String functionNumber){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            // 只有一级代理商才能该操作
            if (loginAgent.getAgentLevel() != 1){
                return new ResponseBean("只有一级代理商才能操作删除功能管控配置");
            }
            return new ResponseBean(agentFunctionService.deleteSubAgentFunctionConfig(loginAgent, agentNo, functionNumber));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/findAgentInfoByAgentNo")
    public ResponseBean findAgentInfoByAgentNo(String agentNo){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            // 只有一级代理商才能该操作
            if (loginAgent.getAgentLevel() != 1){
                return new ResponseBean("只有一级代理商才能操作");
            }
            return new ResponseBean(agentFunctionService.findAgentInfoByAgentNo(loginAgent, agentNo));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
    /**
     * 一级代理商:
     * 		030打开 有解绑权限
     * 		031打开 有绑定权限
     * 二级代理商:
     * 		032打开的前提下:
     * 			002打开 有解绑权限
     * 			003打开 有绑定权限
     */
    @RequestMapping("/showButton")
    public Map<String,Object> showUntiedButton(){
    	Map<String,Object> map = new HashMap<>();
    	Boolean untiedFlag = false;
    	Boolean bindFlag = false;
    	AgentInfo loginAgent = agentInfoService.selectByPrincipal();
    	String entityId = loginAgent.getAgentNo();
    	String parentId = loginAgent.getParentId();
    	Map<String, Object> map30 = agentFunctionService.selectFunctionManage("030");
    	Map<String, Object> map31 = agentFunctionService.selectFunctionManage("031");
    	Map<String, Object> map32 = agentFunctionService.selectFunctionManage("032");
    	String functionSwitch30 = map30.get("function_switch").toString();
    	String functionSwitch31 = map31.get("function_switch").toString();
    	String functionSwitch32 = map32.get("function_switch").toString();
    	String agentControl30 = map30.get("agent_control").toString();//代理商控制开关
    	String agentControl31 = map31.get("agent_control").toString();
    	String agentControl32 = map32.get("agent_control").toString();
    	/**
    	 *  1.两个开关都打开且设置里面存在的代理商 显示
			2.功能开关打开,代理商控制开关关闭,所有的都显示
			3.功能开关关闭,都不显示
    	 */
    	if (loginAgent.getAgentLevel() == 1) {
    		if (("1".equals(functionSwitch30) && "1".equals(agentControl30) && agentFunctionService.countAgentFunctionManage(entityId,"030"))
    				|| ("1".equals(functionSwitch30) && "0".equals(agentControl30))) {
    			untiedFlag = true;
			}
    		if (("1".equals(functionSwitch31) && "1".equals(agentControl31) && agentFunctionService.countAgentFunctionManage(entityId,"031"))
    				|| ("1".equals(functionSwitch31) && "0".equals(agentControl31))) {
    			bindFlag = true;
    		}
		}
    	if (loginAgent.getAgentLevel() == 2 && "1".equals(functionSwitch32)) {
			if(agentFunctionService.countAgentFunction(parentId, "002") && agentFunctionService.countAgentFunction(entityId, "002")){
				untiedFlag = true;
			}
			if(agentFunctionService.countAgentFunction(parentId, "003") && agentFunctionService.countAgentFunction(entityId, "003")){
				bindFlag = true;
			}
		}
    	map.put("untiedFlag", untiedFlag);
    	map.put("bindFlag", bindFlag);
		return map;
    }
}
