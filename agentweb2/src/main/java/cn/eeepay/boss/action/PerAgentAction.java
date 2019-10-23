package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.PaAfterSale;
import cn.eeepay.framework.model.PaCashBackDetail;
import cn.eeepay.framework.model.PaSnBack;
import cn.eeepay.framework.model.PaUserInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.PaChangeLogService;
import cn.eeepay.framework.service.PerAgentService;

/**
 * @author MXG
 * create 2018/07/11
 */
@Controller
@RequestMapping("/perAgent")
public class PerAgentAction {

    private Logger log = LoggerFactory.getLogger(PerAgentAction.class);
    @Resource
    private AgentInfoService agentInfoService;

    @Resource
    private PerAgentService perAgentService;
    
    @Resource
    private PaChangeLogService paChangeLogService;
    
    /**
     * 查询
     * @param info
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectPaUserByParam")
    @ResponseBody
    public Map<String, Object> queryUserByParam(
            @RequestBody PaUserInfo info,
            @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){

        Map<String, Object> msg = new HashMap<>();
        try {
            msg = perAgentService.queryUserByParam(info, pageNo, pageSize);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("盟主列表查询失败", e);
        }
        return msg;
    }
    /**
     * 售后记录
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectPaAfterSale")
    @ResponseBody
    public Map<String, Object> selectPaAfterSale(@RequestBody PaAfterSale baseInfo,@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
    	Map<String, Object> msg = new HashMap<>();
    	try {
    		msg = perAgentService.selectPaAfterSale(baseInfo, pageNo, pageSize);
    	} catch (Exception e) {
    		msg.put("status", false);
    		msg.put("msg", "查询失败");
    		log.error("售后订单查询失败", e);
    	}
    	return msg;
    }
    /**
     * SN回拨记录
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectSnBackByParam")
    @ResponseBody
    public Map<String, Object> selectSnBackByParam(@RequestBody PaSnBack baseInfo,@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
    	Map<String, Object> msg = new HashMap<>();
    	try {
    		msg = perAgentService.selectSnBackByParam(baseInfo, pageNo, pageSize);
    	} catch (Exception e) {
    		msg.put("status", false);
    		msg.put("msg", "查询失败");
    		log.error("SN回拨记录查询失败", e);
    	}
    	return msg;
    }

    /**
     * 根据回拨记录单号查询多台机具
     * @param orderNo
     * @return
     */
    @RequestMapping("/selectSnByOrder")
    @ResponseBody
    public List<Map<String,Object>> selectSnByOrder(String orderNo){
        List<Map<String,Object>> list = new ArrayList<>();
        //权限校验:校验当前登录的代理商是否有权限查询该订单号
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent == null) {
            return list;
        }
        PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(loginAgent.getAgentNo());
        String id = perAgentService.selectPaTerminalBackByOrderNo(paUserInfo.getUserNode(), orderNo);
        if(StringUtils.isBlank(id)){
            return list;
        }
    	try {
    		list = perAgentService.selectSnByOrder(orderNo);
    	} catch (Exception e) {
    		log.error("查询失败", e);
    	}
    	return list;
    }
    /**
     * 确定接收 1 ,拒绝接收 2
     * @param orderNo
     * @return
     */
    @SystemLog(description = "超级盟机具更新状态")
    @RequestMapping("/updateStatus")
    @ResponseBody
    public Map<String,Object> updateStatus(String orderNo,String status){
    	Map<String,Object> map = new HashMap<>();
    	try {
    		if(perAgentService.updateStatus(orderNo,status) >= 2){
    			map.put("status", true);
    			map.put("msg", "操作成功");
    			return map;
    		}
    		map.put("status", false);
			map.put("msg", "操作失败");
			return map;
    	} catch (Exception e) {
    		map.put("status", false);
    		map.put("msg", "操作异常");
    		log.error("操作异常", e);
    	}
    	return map;
    }
    /**
     * 导出sn回拨记录
     * @param param
     * @param response
     * @param request
     */
    @SystemLog(description = "导出sn回拨记录")
    @RequestMapping("/exportSnBack")
    @ResponseBody
    public void exportSnBack(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response, HttpServletRequest request) {
    	try {
    		baseInfo = new String(request.getParameter("baseInfo").getBytes("ISO8859-1"), "UTF-8");
    		PaSnBack info = JSONObject.parseObject(baseInfo, PaSnBack.class);
    		perAgentService.exportSnBack(info, response, request);
    	} catch (Exception e) {
    		log.error("导出失败",e);
    	}
    }
    /**
     * 导出售后订单
     * @param param
     * @param response
     * @param request
     */
    @SystemLog(description = "导出售后订单")
    @RequestMapping("/exportAfterSale")
    @ResponseBody
    public void exportAfterSale(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response, HttpServletRequest request) {
    	try {
    		baseInfo = new String(request.getParameter("baseInfo").getBytes("ISO8859-1"), "UTF-8");
    		PaAfterSale info = JSONObject.parseObject(baseInfo, PaAfterSale.class);
    		perAgentService.exportAfterSale(info, response, request);
    	} catch (Exception e) {
    		log.error("导出失败",e);
    	}
    }

    /**
     * 导出
     * @param param
     * @param response
     * @param request
     * @throws Exception
     */
    @SystemLog(description = "导出超级盟用户数据")
    @RequestMapping("/exportPerAgentUser")
    @ResponseBody
    public void exportPerAgentUser(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response, HttpServletRequest request) {
        try {
        	baseInfo = new String(request.getParameter("baseInfo").getBytes("ISO8859-1"), "UTF-8");
        	PaUserInfo info = JSONObject.parseObject(baseInfo, PaUserInfo.class);
            perAgentService.exportPerAgentUser(info, response, request);
        } catch (Exception e) {
        	log.error("盟主列表导出失败",e);
        }
    }
    /**
     * 盟友活动返现明细查询
     * @param info
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectPaCashBackDetail")
    @ResponseBody
    public Map<String, Object> selectPaCashBackDetail(@RequestBody PaCashBackDetail info,@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        Map<String, Object> msg = new HashMap<>();
        try {
            msg = perAgentService.selectPaCashBackDetail(info, pageNo, pageSize);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("盟友活动返现明细查询失败", e);
        }
        return msg;
    }
    /**
     * 盟友活动返现明细导出
     * @param param
     * @param response
     * @param request
     */
    @RequestMapping("/exportCashBackDetail")
    @ResponseBody
    public void exportCashBackDetail(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response,HttpServletRequest request) {
    	try {
    		baseInfo = new String(request.getParameter("baseInfo").getBytes("ISO8859-1"), "UTF-8");
	    	PaCashBackDetail info = JSONObject.parseObject(baseInfo, PaCashBackDetail.class);
    		perAgentService.exportCashBackDetail(info, response,request);
    	} catch (Exception e) {
    		log.error("导出失败",e);
    	}
    }

    /**
     * 修改用户分润等级
     * @param params
     * @return
     */
    @SystemLog(description = "修改用户分润等级")
    @RequestMapping("/updateShareLevel")
    @ResponseBody
    public Map<String, Object> updateShareLevel(@RequestBody String params){
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(params);
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            PaUserInfo loginUser = perAgentService.selectUserByAgentNo(loginAgent.getAgentNo());
            String createUser = loginUser.getUserCode();
            String userCode = jsonObject.getString("user_code");
            Integer afterLevel = jsonObject.getInteger("after_level");
            String agentNo = jsonObject.getString("agentNo");

            boolean flag = accessCheck(userCode, afterLevel, loginAgent);
            if(!flag){
                result.put("status", false);
                result.put("msg", "非法操作");
                return result;
            }

            if (StringUtils.isBlank(userCode)) {
                Map<String, String> map = perAgentService.selectByAgentNo(agentNo);
                userCode = map.get("user_code");
            }
            String param = "create_user=" + createUser + "&userCode=" + userCode + "&after_level="+ afterLevel;
            result = perAgentService.updateShareLevel(param);
        } catch (Exception e) {
            result.put("status", false);
            log.error("修改用户分润等级失败，用户编码为" + jsonObject.getString("userCode"));
        }
        return result;
    }

    private boolean accessCheck(String userCode, Integer afterLevel, AgentInfo loginAgent){
        //权限验证 1.user_code 必须属于当前代理商或其下级  2. after_level必须处于其可以调整的范围
        boolean flag = false;
        PaUserInfo userInfo = perAgentService.selectByUserCode(userCode);
        if(!userInfo.getAgentNode().startsWith(loginAgent.getAgentNode())){
            return false;
        }
        //查询所有可调整的分润级别
        List<Integer> list = perAgentService.selectShareLevelList(loginAgent.getAgentOem(), loginAgent.getAgentShareLevel());
        //不能小于等于当前分润级别
        if(afterLevel <= Integer.parseInt(userInfo.getShareLevel())){
            return false;
        }
        for (Integer s : list) {
            if(afterLevel == s){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 获取分润下拉选项
     * @return
     */
    @RequestMapping("/shareLevelList")
    @ResponseBody
    public Map<String, Object> shareLevelList(){
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map> shareLevelList = perAgentService.getShareLevelList();
            result.put("status", true);
            result.put("shareLevelList", shareLevelList);
        } catch (Exception e) {
            result.put("status", false);
            log.error("获取分润下拉选项失败", e);
        }
        return result;
    }


    @RequestMapping(value = "/selectChildPaUser.do")
    public @ResponseBody Map<String, Object> selectChildPaUser() throws Exception {
       Map<String, Object> map = new HashMap<String, Object>();
        List<PaUserInfo> list = null;
        try {
            Map<String, Object> msg = new HashMap<>();
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            if(loginAgent != null) {

            }else {
                msg.put("status", false);
                msg.put("msg", "请先登录");
                return msg;
            }
            //查询登录代理商对应的人人代理用户信息
            PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(loginAgent.getAgentNo());
            //查询所有下级盟主，不包括自己
            list = perAgentService.selectChildPaUser(paUserInfo);
            map.put("paUserList", list);
        } catch (Exception e) {
            log.error("根据代理商id查询所有盟主失败！",e);
        }
        return map;
    }

    /**
     * 查询到用户信息和结算卡信息,用于升级大盟主时新增代理商回显
     * @param params
     * @return
     */
    @RequestMapping(value = "/upgradePerAgent.do")
    @ResponseBody
    public Map<String, Object> upgradePerAgent(@RequestBody String params){
    	JSONObject jsonObject = JSONObject.parseObject(params);
    	String userCode = jsonObject.getString("userCode");
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
    		PaUserInfo paUserInfo = perAgentService.selectByUserCode(userCode);
    		Map<String,Object> userCardMap = perAgentService.selectPaUserCardByUserCode(userCode);
    		if (userCardMap != null) {
    			userCardMap.put("cnaps", userCardMap.get("cnaps") == null ? "" : Long.valueOf(userCardMap.get("cnaps").toString()));
			}
    		map.put("paUserInfo", paUserInfo);
    		map.put("paUserCard", userCardMap);
    	} catch (Exception e) {
    		log.error("查询异常！",e);
    	}
    	return map;
    }

    /**
     * 设置是否允许调比例
     * @param params
     * @return
     */
    @SystemLog(description = "设置是否允许调比例")
    @RequestMapping("/updateCanProfitChange.do")
    @ResponseBody
    public boolean updateCanProfitChange(@RequestBody String params){
        JSONObject jsonObject = JSONObject.parseObject(params);
    	String canProfitChange = jsonObject.getString("canProfitChange");
    	String userCode = jsonObject.getString("userCode");
        try {
        	if(perAgentService.updateCanProfitChange(canProfitChange,userCode) > 0){
        		return true;
        	}
        } catch (Exception e) {
            log.error("操作异常" + jsonObject.getString("userCode"));
        }
        return false;
    }
    @RequestMapping(value = "/selectStatus.do")
    @ResponseBody
    public Integer selectStatus(){
    	try {
    		return perAgentService.selectStatus();
    	} catch (Exception e) {
    		log.error("查询异常！",e);
    	}
    	return null;
    }
    
    /**
     * 售后订单立即处理
     * @param info
     * @return
     */
    @SystemLog(description = "售后订单立即处理")
    @RequestMapping(value = "/updateNowAfterSale.do")
    @ResponseBody
    public Map<String, Object> updateNowAfterSale(@RequestBody String info){
    	JSONObject jsonObject = JSONObject.parseObject(info);
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
    		if (perAgentService.updateNowAfterSale(jsonObject) > 0) {
    			map.put("status", true);
    			map.put("msg", "操作成功");
    			return map;
			}
    		map.put("status", false);
    		map.put("msg", "操作失败");
    		return map;
    	} catch (Exception e) {
    		log.error("操作异常！",e);
    		map.put("status", false);
    		map.put("msg", "操作异常");
    	}
    	return map;
    }
}
