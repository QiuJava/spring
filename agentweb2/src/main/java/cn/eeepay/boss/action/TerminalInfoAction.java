package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.RespUtil;
import cn.eeepay.framework.util.Result;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.ObjectUtils;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/terminalInfo")
public class TerminalInfoAction {

    private static final Logger log = LoggerFactory.getLogger(TerminalInfoAction.class);

    @Resource
    public TerminalInfoService terminalInfoService;
    @Resource
    public AgentInfoService agentInfoService;
    @Resource
    private PerAgentService perAgentService;

    @Resource
    private AgentFunctionAction agentFunctionAction;

    private Page<TerminalInfo> page = new Page<TerminalInfo>();
    
    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;
    @Resource
    private AccessService accessService;
    @Resource
    private AgentTerminalOperateService agentTerminalOperateService;


    /**
     * 活动考核机具查询
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectTerActivityCheck.do")
    public @ResponseBody Object selectTerActivityCheck(@RequestParam("info") String param,
                                                       @ModelAttribute("page") Page<TerActivityCheck> page) {
        try {
            log.info("活动考核机具查询参数  ===> {}", param);
            TerActivityCheck terActivityCheck = JSON.parseObject(param, TerActivityCheck.class);
            terminalInfoService.selectTerActivityCheck(page, terActivityCheck);
        } catch (Exception e) {
            log.error("活动考核机具查询异常", e);
        }
        return page;
    }
    @RequestMapping("/exportTerActivityCheck.do")
    @ResponseBody
    public void exportTerActivityCheck(@RequestParam("info") String info, HttpServletResponse response){
        try {
            TerActivityCheck terActivityCheck = JSON.parseObject(info, TerActivityCheck.class);
            terminalInfoService.exportTerActivityCheck(response, terActivityCheck);
        } catch (Exception e){
            e.printStackTrace();
            log.error("活动考核机具列表导出异常", e);
        }
    }

    @RequestMapping("/updateActivityBatch")
    @ResponseBody
    public Result updateActivityBatch(String sns,String activityTypeNo) {
    	Result result = new Result();
    	try {
    		List<TerminalInfo> snList = JSON.parseArray(sns, TerminalInfo.class);
    		UpdateActivityBatchResult batchResult = terminalInfoService.updateActivityBatch(snList,activityTypeNo);
    		result.setStatus(true);
    		result.setData(batchResult);
    	} catch (Exception e) {
    		result.setStatus(false);
    		log.error("系统异常", e);
    	}
    	return result;
    }
    
    @RequestMapping("/acvtivityType")
    @ResponseBody
    public Result acvtivityType(String sn) {
    	Result result = new Result();
    	try {
    		List<HardwareAcvitityType> list = terminalInfoService.getActivityType(sn);
    		result.setStatus(true);
    		result.setData(list);
		} catch (Exception e) {
			result.setStatus(false);
			log.error("系统异常", e);
		}
    	return result;
    }
    
    @RequestMapping("/acvtivityTypes")
    @ResponseBody
    public Result acvtivityTypes(String sns) {
    	Result result = new Result();
    	try {
    		List<TerminalInfo> snList = JSON.parseArray(sns, TerminalInfo.class);
    		List<HardwareAcvitityType> list = terminalInfoService.getActivityType(snList);
    		result.setStatus(true);
    		result.setData(list);
		} catch (Exception e) {
			result.setStatus(false);
			log.error("系统异常", e);
		}
    	return result;
    }
    
    @RequestMapping("/updateActivity")
    @ResponseBody
    public Result updateActivity(String sn,String activityTypeNo) {
    	Result result = new Result();
    	try {
    		terminalInfoService.updateActivity(sn,activityTypeNo);
    		result.setStatus(true);
    	} catch (Exception e) {
    		result.setStatus(false);
    		if (e instanceof AgentWebException) {
    			result.setMsg(e.getMessage());
    			log.info(e.getMessage());
			}else {
				log.error("系统异常", e);
			}
    	}
    	return result;
    }
    
    //初始化查询
    @RequestMapping(value = "/selectAllInfo.do")
    public @ResponseBody
    Object selectAllInfo(@ModelAttribute("page") Page<TerminalInfo> page) throws Exception {

        //return selectByCondition("",page);
       List<TerminalInfo> list = null;
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            TerminalInfo terminalInfo = new TerminalInfo();
            AgentInfo ais = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
            terminalInfo.setBool(ais.getAgentNode() + "%");
            list = terminalInfoService.selectByParam(page, terminalInfo);
        } catch (Exception e) {
            log.error("初始化失败-----", e);
            return false;
        }
        log.info("111page.getPageNo()="+page.getPageNo()+"--page.getPageSize()="+page.getPageSize()+"--page.getTotalPages()="+page.getTotalPages());
        return page;
    }

    @RequestMapping(value = "/selectUserCodeLists")
    public @ResponseBody
    Object selectUserCodeLists() throws Exception {
        List<TerminalInfo> list = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            AgentInfo ais = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
            list = terminalInfoService.selectUserCodeLists(ais.getAgentNode());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询userCode列表失败！");
        }
        return list;
    }

    //详情查询
    @RequestMapping(value = "/selectObjInfo.do")
    @ResponseBody
    public ResponseBean selectObjInfo(String ids) throws Exception {
        Long id = Long.valueOf(ids);
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String entityId = principal.getUserEntityInfo().getEntityId();
            if (!agentInfoService.terminalIsBelongToAgent(ids, entityId)) {
                throw new AgentWebException(String.format("该机具 %s 不属于代理商 %s 的,无权进行查询.", ids, entityId));
            }
            TerminalInfo tis = terminalInfoService.selectObjInfo(id,entityId);
            if (tis.getType() != null) {
                tis.setTypeName(tis.getTypeName() + tis.getVersionNu());
            }
            return new ResponseBean(tis);
        } catch (Exception e) {
            return new ResponseBean(e);
        }

    }

    //条件查询
    @RequestMapping(value = "/selectByCondition.do")
    public @ResponseBody
    Object selectByCondition(@RequestParam("info") String param, @ModelAttribute("page") Page<TerminalInfo> page) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        List<TerminalInfo> list = null;
        TerminalInfo terminalInfo = JSON.parseObject(param, TerminalInfo.class);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (!"".equals(terminalInfo.getSn())) {
                terminalInfo.setSn("%" + terminalInfo.getSn() + "%");
            }
            if (!"".equals(terminalInfo.getAgentName())) {
                if (!accessService.canAccessTheAgent(terminalInfo.getAgentName(), false)) {
                    jsonMap.put("result", false);
                    return jsonMap;
                }
                AgentInfo ais = agentInfoService.selectByName(terminalInfo.getAgentName());
                if (ais != null) {
                    if ("1".equals(terminalInfo.getBool())) {//包含下级
//						terminalInfo.setAgentName(terminalInfo.getAgentName()+"%");
                        terminalInfo.setBool(ais.getAgentNode() + "%");
                    } else {
                        terminalInfo.setBool(ais.getAgentNode());
                    }
                } else {
                    terminalInfo.setBool("---000000");
                }
            } else {
                AgentInfo ais = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
                terminalInfo.setBool(ais.getAgentNode() + "%");
            }
            list = terminalInfoService.selectByParam(page, terminalInfo);
            jsonMap.put("list", page);
            jsonMap.put("result", true);
        } catch (Exception e) {
            log.error("机具条件查询出错-----", e);
            jsonMap.put("result", false);
            jsonMap.put("list", page);
        }
        log.info("222page.getPageNo()="+page.getPageNo()+"--page.getPageSize()="+page.getPageSize()+"--page.getTotalPages()="+page.getTotalPages());
        return jsonMap;
    }


    //下发
    @SystemLog(description = "机具下发")
    @RequestMapping(value = "/distributionTerminal.do")
    public @ResponseBody
    Object distributionTerminal(@RequestParam("param") String param, @ModelAttribute("page") Page<TerminalInfo> page) throws Exception {
        log.info("=====接口参数 param = " + param + "=======");
        JSONObject json = JSON.parseObject(param);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            int num = 0;
            int count = 0;
            String agentNo = json.getString("agentNo");
            String entityId = principal.getUserEntityInfo().getEntityId();
            AgentInfo entityInfo = agentInfoService.selectByagentNo(entityId);
            if (!StringUtils.equals(entityInfo.getAgentType(), "11") &&
                    !accessService.canAccessTheAgent(agentNo, false)) {
                jsonMap.put("result", false);
                jsonMap.put("message", "无权操作");
                return jsonMap;
            }
            String userCode = json.getString("userCode");
            Map<String, String> map = null;
            AgentInfo ais = null;
            String user_type = "2";//机构下发给盟主
            if ("11".equals(entityInfo.getAgentType())) {
                ais = new AgentInfo();
                /*map = perAgentService.selectByAgentNo(agentNo);
                Map<String, String> entityMap = perAgentService.selectByAgentNo(entityId);
                if (map == null || entityMap == null) {
                    log.info("根据代理商编号" + agentNo + "或entityId" + "在人人代理没有查询到数据");
                    jsonMap.put("result", false);
                    jsonMap.put("message", "下发失败！");
                    return jsonMap;
                }*/
                //查询登录代理商对应的人人代理用户信息
                PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(entityId);
                PaUserInfo info = perAgentService.selectByUserCode(userCode);
                if (paUserInfo != null && info != null) {
                    if ("1".equals(paUserInfo.getUserType())) {
                        if (info.getUserNode().startsWith(paUserInfo.getUserNode())) {
                            ais.setUserCode(info.getUserCode());
                            ais.setAgentNo(info.getAgentNo());
                            ais.setAgentNode(info.getAgentNode());
                            entityInfo.setUserCode(paUserInfo.getUserCode());
                            entityInfo.setUserType(paUserInfo.getUserType());
                        } else {
                            jsonMap.put("result", false);
                            jsonMap.put("message", "下发失败:用户非直营大盟主或者盟主");
                            return jsonMap;
                        }
                    } else if ("2".equals(paUserInfo.getUserType())) {
                        if (info.getParentId().equals(paUserInfo.getUserCode())) {
                            ais.setUserCode(info.getUserCode());
                            ais.setAgentNo(info.getAgentNo());
                            ais.setAgentNode(info.getAgentNode());
                            entityInfo.setUserCode(paUserInfo.getUserCode());
                            entityInfo.setUserType(paUserInfo.getUserType());
                        } else {
                            jsonMap.put("result", false);
                            jsonMap.put("message", "下发失败:用户非直营大盟主或者盟主");
                            return jsonMap;
                        }
                    } else {
                        jsonMap.put("result", false);
                        jsonMap.put("message", "下发失败:用户非机构或者大盟主");
                        return jsonMap;
                    }

                } else {
                    log.info("根据代理商编号" + agentNo + "或entityId" + "在人人代理没有查询到数据");
                    jsonMap.put("result", false);
                    jsonMap.put("message", "下发失败！");
                    return jsonMap;
                }


                entityInfo.setUserCode(paUserInfo.getUserCode());
                if (entityInfo.getAgentLevel() == 2) {//盟主下发给盟友
                    user_type = "3";
                }
            } else {
                ais = agentInfoService.selectByagentNo(agentNo);
            }
            List<TerminalInfo> terminalInfo1 = JSON.parseArray(json.getJSONArray("list").toJSONString(), TerminalInfo.class);
            boolean flag = false;
            for (TerminalInfo terminalInfo : terminalInfo1) {
                if (!accessService.canAccessTheTerminalById(terminalInfo.getId(), true)) {
                    jsonMap.put("result", false);
                    jsonMap.put("message", "无权操作");
                    return jsonMap;
                }
                terminalInfo.setStartTime(new Date());
                terminalInfo.setOpenStatus("1");
                terminalInfo.setAgentNo(ais.getAgentNo());
                terminalInfo.setAgentNode(ais.getAgentNode());
                terminalInfo.setDownDate(new Date() );
                terminalInfo.setReceiptDate(new Date());
                num += terminalInfoService.updateSolutionById(terminalInfo);

                terminalInfoService.addAgentActivity(terminalInfo.getSn(), agentNo);

                if (num > 0 && "11".equals(entityInfo.getAgentType())) {
                	String sn = terminalInfo.getSn();
                	//判断机具是否被锁定
                	Map<String, Object> paTerInfoMap = terminalInfoService.selectPaTerInfo(sn);
                	if (paTerInfoMap != null && "1".equals(paTerInfoMap.get("callback_lock").toString())) {
                		jsonMap.put("result", false);
                        jsonMap.put("message", "机具已被锁定,不可以再下发");
                        log.info("锁定状态机具 sn ==> " + sn + "=======不可以再下发==");
                        return jsonMap;
					}
                    count += terminalInfoService.updateTer(ais.getAgentNo(), ais.getUserCode(), sn, entityInfo.getUserCode());

                    //记录机具下发时间 接收时间

                    //调明聪机具下发接口
                   /* log.info("===========进入人人代理下发机具==========");
                    String result = ClientInterface.lowerHairTermianl(entityInfo, ais, user_type, terminalInfo.getSn(), map.get("user_code"));
                    log.info("=====接口返回 result = " + result + "=======");
                    if (StringUtils.isNotBlank(result)) {
                        JSONObject jsonResult = JSON.parseObject(result);
                        if ("200".equals(jsonResult.getString("status"))) {//要求一个一个下发,下发成功能这边才能下发
                            flag = true;
                        }
                    }*/
                }
                if (num>0){
                    Date date = new Date();
                    //记录机具下发时间
                    agentTerminalOperateService.insertAgentTerminalOperation(entityInfo.getAgentNo(),terminalInfo.getSn(),"1","2",date);
                    //机具接收时间
                    agentTerminalOperateService.insertAgentTerminalOperation(agentNo,terminalInfo.getSn(),"1","1",date);
                }
            }

            if ("11".equals(entityInfo.getAgentType())) {

                if (count == num) {
                    jsonMap.put("result", true);
                    jsonMap.put("object", selectAllInfo(page));
                } else {
                    jsonMap.put("result", false);
                    jsonMap.put("message", "分配失败！！！！！");
                    log.info("===人人代理机具下发失败=====");
                }
            } else {
                if (num > 0) {
                    jsonMap.put("result", true);
                    jsonMap.put("object", selectAllInfo(page));
                } else {
                    jsonMap.put("result", false);
                    jsonMap.put("message", "分配失败！！！！！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("机具分配出错-----", e);
            jsonMap.put("result", false);
        }
        return jsonMap;
    }


    //机具批量下发==tgh308=====================
    @SystemLog(description = "机具批量下发")
    @RequestMapping(value = "/distributionTerminalBatch.do")
    public @ResponseBody
    Object distributionTerminalBatch(@RequestParam("param") String param) throws Exception {
        log.info("=====接口参数 param = " + param + "=======");
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String entityId = principal.getUserEntityInfo().getEntityId();
        AgentInfo entityInfo = agentInfoService.selectByagentNo(entityId);
        JSONObject json = JSON.parseObject(param);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            String agentNo = json.getString("agentNo");
            String userCode = json.getString("userCode");
            String snStart1 = json.getString("snStart1");
            String snEnd1 = json.getString("snEnd1");
            jsonMap = terminalInfoService.UpdateTerminalInfoBySn(snStart1, snEnd1, agentNo, userCode);
            Integer num = Integer.valueOf(jsonMap.get("num").toString());
            Integer count = Integer.valueOf(jsonMap.get("count").toString());
            if ("11".equals(entityInfo.getAgentType())) {
                if (num > 0 && count > 0) {
                    jsonMap.put("message", "下发成功");
                    jsonMap.put("object", selectAllInfo(page));
                } else {
                    jsonMap.put("result", false);
                    jsonMap.put("message", "下发失败");
                    log.info("人人代理机具下发失败");
                }
            } else {
                if (num > 0) {
                    jsonMap.put("result", true);
                    jsonMap.put("message", "下发成功");
                    jsonMap.put("object", selectAllInfo(page));
                } else {
                    jsonMap.put("result", false);
                    jsonMap.put("message", "分配失败！！！！！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("机具分配出错", e);
            jsonMap.put("result", false);
            jsonMap.put("message", "下发失败或存在不可下发机具!");
        }
        return jsonMap;
    }

    @SystemLog(description = "机具回收")
    @RequestMapping(value = "/recoveryTerminal.do")
    public @ResponseBody
    Object recoveryTerminal(@RequestParam("param") String param, @ModelAttribute("page") Page<TerminalInfo> page) throws Exception {
        JSONObject json = JSON.parseObject(param);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            int num = 0;
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            AgentInfo ais = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
            List<TerminalInfo> terminalInfo1 = JSON.parseArray(json.getJSONArray("list").toJSONString(), TerminalInfo.class);
            String agentNode = ais.getAgentNode();

            if ("11".equals(ais.getAgentType())) {
                //查询登录代理商对应的人人代理用户信息
                PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(ais.getAgentNo());
                for (TerminalInfo terminalInfo : terminalInfo1) {
                    String sn = terminalInfo.getSn();
                    Map<String, Object> paTerInfo = terminalInfoService.selectPaTerInfo(sn);
                    if (paTerInfo != null && "0".equals(paTerInfo.get("callback_lock").toString())) {
                        if ((Integer) paTerInfo.get("status") == 2) {
                            terminalInfoService.recoverById(paUserInfo, paTerInfo.get("id") + "");

                            terminalInfo.setStartTime(new Date());
                            terminalInfo.setOpenStatus("1");
                            terminalInfo.setAgentNo(ais.getAgentNo());
                            terminalInfo.setAgentNode(ais.getAgentNode());
                            num += terminalInfoService.updateSolutionById(terminalInfo);

                        } else {
                            continue;
                        }
                    }

                    /*if (StringUtil.isBlank(terminalInfo.getMerchantNo())) {//商户编号为空,且只能回收直属下级的机具tgh0615
                        String newAgentNode = terminalInfoService.selectAgentNode(terminalInfo.getSn());
                        String node = newAgentNode.substring(0, newAgentNode.lastIndexOf("-"));
                        if (!(node.substring(0, node.lastIndexOf("-") + 1)).equals(agentNode)) {
                            continue;
                        }
                        terminalInfo.setStartTime(new Date());
                        terminalInfo.setOpenStatus("1");
                        terminalInfo.setAgentNo(ais.getAgentNo());
                        terminalInfo.setAgentNode(ais.getAgentNode());
                        num += terminalInfoService.updateSolutionById(terminalInfo);
                    }*/
                }
            } else {
                for (TerminalInfo terminalInfo : terminalInfo1) {
                    if (StringUtil.isBlank(terminalInfo.getMerchantNo())) {//商户编号为空,且只能回收直属下级的机具tgh0615
                        String newAgentNode = terminalInfoService.selectAgentNode(terminalInfo.getSn());
                        String node = newAgentNode.substring(0, newAgentNode.lastIndexOf("-"));
                        if (!(node.substring(0, node.lastIndexOf("-") + 1)).equals(agentNode)) {
                            continue;
                        }
                        terminalInfo.setStartTime(new Date());
                        terminalInfo.setOpenStatus("1");
                        terminalInfo.setAgentNo(ais.getAgentNo());
                        terminalInfo.setAgentNode(ais.getAgentNode());
                        terminalInfo.setDownDate(null);
                        terminalInfo.setReceiptDate(null);
                        num += terminalInfoService.updateSolutionById(terminalInfo);
                    }
                }
            }

            if (num > 0) {
                jsonMap.put("result", true);
                jsonMap.put("object", selectAllInfo(page));
            } else {
                jsonMap.put("result", false);
                jsonMap.put("message", "回收失败！！！！！");
            }
        } catch (Exception e) {
            log.error("机具下发出错-----", e);
            jsonMap.put("result", false);
        }
        return jsonMap;
    }


    public String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getStringCellValue();
        }
        return null;
    }

    /**
     * 获取当前用户所在的代理商id
     */
    @RequestMapping("/getCurrentAgent.do")
    public void getCurrentAgent(HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        map.put("agentId", principal.getUserEntityInfo().getEntityId());
        RespUtil.renderJson(response, map);
    }

    //228tgh查询所有机具活动
    @RequestMapping(value = "/selectAllActivityType.do")
    public @ResponseBody
    List<Map<String, String>> selectAllActivityType() throws Exception {
        List<Map<String, String>> map = null;
        try {
            map = terminalInfoService.selectAllActivityType();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询机具活动失败-----", e);
        }

        return map;
    }
    
    // 解绑
    @SystemLog(description = "机具解绑")
 	@RequestMapping(value = "/untiedById.do")
 	public @ResponseBody Object unbundlingById(@RequestParam("id") Long id) {
 		log.info("=====进入机具解绑=======");
 		Map<String, Object> msg = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (!accessService.canAccessTheTerminalById(id, loginAgent.getAgentLevel() != 1)){
            msg.put("status", false);
            msg.put("msg", "无权操作");
            return msg;
        }
        Map<String, Object> map1 = agentFunctionAction.showUntiedButton();
        if (map1 == null || map1.get("untiedFlag") == null || !Boolean.valueOf(ObjectUtils.toString(map1.get("untiedFlag")))) {
            msg.put("status", false);
            msg.put("msg", "您没有权限操作解绑机具操作");
            return msg;
        }
 		try {
 			msg = terminalInfoService.untiedById(id);
 		} catch (Exception e) {
 			log.error("机具解绑出错-----", e);
 			msg.put("status", false);
 			msg.put("msg", "解绑失败");
 		}
 		return msg;
 	}
 	
 	// 绑定机具
    @SystemLog(description = "绑定机具")
 	@RequestMapping(value = "/bindingTerminal.do")
 	@ResponseBody
 	public Map<String,Object> bindingTerminal(@RequestParam("param") String param) {
 		Map<String,Object> map = new HashMap<>();
 		JSONObject json = JSON.parseObject(param);
 		try {
 			String merNo = json.getString("merNo");
 			String sn = json.getString("sn");
 			String id = json.getString("id");
 			String bpId = json.getString("bpId");
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            if (!accessService.canAccessTheTerminalBySn(sn, loginAgent.getAgentLevel() != 1) ||
                    !accessService.canAccessTheMerchant(merNo, loginAgent.getAgentLevel() != 1)){
                map.put("status", false);
                map.put("msg", "无权操作");
                return map;
            }
            Map<String, Object> map1 = agentFunctionAction.showUntiedButton();
            if (map1 == null || map1.get("bindFlag") == null || !Boolean.valueOf(ObjectUtils.toString(map1.get("bindFlag")))) {
                map.put("status", false);
                map.put("msg", "您没有权限操作绑定机具操作");
                return map;
            }
            if(StringUtils.isBlank(merNo)){
 				map.put("status", false);
 				map.put("msg", "请输入商户编号");
 				return map;
 			}
 			if("-1".equals(bpId) || StringUtils.isBlank(bpId)){
 				map.put("status", false);
 				map.put("msg", "请输入业务产品");
 				return map;
 			}
            MerchantInfo merchantInfo=terminalInfoService.selectMerchantDetail(merNo);
            HardwareProduct hardwareProduct=terminalInfoService.selectHardwareProductBySn(sn);
            if(merchantInfo!=null&&hardwareProduct!=null){
                if(!merchantInfo.getTeamId().equals(hardwareProduct.getOrgId())){
                    map.put("status", false);
                    map.put("msg", "组织不一致，绑定失败");
                    return map;
                }
            }else {
                map.put("status", false);
                map.put("msg", "组织不一致，绑定失败");
                return map;
            }
 			map = terminalInfoService.updateBindingTerminal(merNo, sn,id,bpId);
 		} catch (Exception e) {
 			log.error("机具绑定异常", e);
 			map.put("status", false);
 			map.put("msg", "机具绑定异常");
 		}
 		return map;
 	}
 	
 	@RequestMapping("/getMbpByMerId")
	@ResponseBody
	public Object getMbpByMerId(String merId) {
		Map<String, Object> jsonMap = new HashMap<>();
        if (!accessService.canAccessTheMerchant(merId, false)){
            jsonMap.put("result", false);
            jsonMap.put("msg", "无权操作");
            return jsonMap;
        }
		List<MerchantBusinessProduct> list = merchantBusinessProductService.selectByParam(merId);
		jsonMap.put("list", list);
		jsonMap.put("result", true);
		return jsonMap;
	}
 	
 	//查询当前登录代理商所有商户(一级查所有,二级查当前)
 	@RequestMapping(value = "/selectAllMerchantInfo.do")
	public @ResponseBody Object selectAllMerchantInfo() {
		List<Map<String,String>> list = new ArrayList<>();
		try {
			list = terminalInfoService.selectAllMerchantInfo();
		} catch (Exception e) {
			log.error("查询异常！",e);
		}
		return list;
	}

    /**
     * 根据商户号查询商户名
     * @return
     */
    @RequestMapping(value = "/selectMerchantName")
    @ResponseBody
    public Map<String, Object> selectMerchantName(@RequestParam("merchantNo") String merchantNo){
        Map<String, Object> result = new HashMap<>();
        String merchantName =  terminalInfoService.selectMerchantNameByMerchantNo(merchantNo);
        result.put("merchantName", merchantName);
        return result;
    }
 	
 	//条件查询
    @RequestMapping(value = "/threeSelectByCondition.do")
    public @ResponseBody
    Object threeSelectByCondition(@RequestParam("info") String param, @ModelAttribute("page") Page<TerminalInfo> page) throws Exception {
    	 Map<String, Object> jsonMap = new HashMap<>();
         TerminalInfo terminalInfo = JSON.parseObject(param, TerminalInfo.class);
         final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         try {
        	  String currAgentNo =principal.getUserEntityInfo().getEntityId();
        	 String selectAgentNo = terminalInfo.getAgentName();
        	// 判断是否授权
             	boolean isAuth = agentInfoService.isAuth(currAgentNo,selectAgentNo);
             	if (!isAuth) {
             		jsonMap.put("result", true);
             		 jsonMap.put("list", page);
             		return jsonMap;
             	}
             	currAgentNo = selectAgentNo;
             if (!"".equals(terminalInfo.getSn())) {
                 terminalInfo.setSn("%" + terminalInfo.getSn() + "%");
             }
             if (!"".equals(terminalInfo.getAgentName())) {
                /* if (!accessService.canAccessTheAgent(terminalInfo.getAgentName(), false)) {
                     jsonMap.put("result", false);
                     return jsonMap;
                 }*/
                 AgentInfo ais = agentInfoService.selectByName(terminalInfo.getAgentName());
                 if (ais != null) {
                     if ("1".equals(terminalInfo.getBool())) {//包含下级
// 						terminalInfo.setAgentName(terminalInfo.getAgentName()+"%");
                         terminalInfo.setBool(ais.getAgentNode() + "%");
                     } else {
                         terminalInfo.setBool(ais.getAgentNode());
                     }
                 } else {
                     terminalInfo.setBool("---000000");
                 }
             } else {
                 AgentInfo ais = agentInfoService.selectByagentNo(currAgentNo);
                 terminalInfo.setBool(ais.getAgentNode() + "%");
             }
             terminalInfoService.selectByParam(page, terminalInfo);
             jsonMap.put("list", page);
             jsonMap.put("result", true);
         } catch (Exception e) {
             log.error("机具条件查询出错-----", e);
             jsonMap.put("result", false);
             jsonMap.put("list", page);
         }
         log.info("222page.getPageNo()="+page.getPageNo()+"--page.getPageSize()="+page.getPageSize()+"--page.getTotalPages()="+page.getTotalPages());
         return jsonMap;
    }
   /**
     * 导出机具列表
     * @param info
     */
    @RequestMapping("/exportTerinalInfo")
//    @SystemLog(description = "导出机具列表")
    @ResponseBody
    public void exportTerinalInfo(@RequestParam("info") String info, HttpServletResponse response, HttpServletRequest request){
        try {
            log.info("开始导出机具列表");
            TerminalInfo terminalInfo = JSON.parseObject(info, TerminalInfo.class);
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!"".equals(terminalInfo.getSn())) {
                    terminalInfo.setSn("%" + terminalInfo.getSn() + "%");
                }
                if (!"".equals(terminalInfo.getAgentName())) {
                    if (!accessService.canAccessTheAgent(terminalInfo.getAgentName(), false)) {
                        //break;
                    }
                    AgentInfo ais = agentInfoService.selectByName(terminalInfo.getAgentName());
                    if (ais != null) {
                        if ("1".equals(terminalInfo.getBool())) {//包含下级
//						terminalInfo.setAgentName(terminalInfo.getAgentName()+"%");
                            terminalInfo.setBool(ais.getAgentNode() + "%");
                        } else {
                            terminalInfo.setBool(ais.getAgentNode());
                        }
                    } else {
                        terminalInfo.setBool("---000000");
                    }
                } else {
                    AgentInfo ais = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
                    terminalInfo.setBool(ais.getAgentNode() + "%");
                }

            terminalInfoService.exportTerinalInfo(response,request, terminalInfo);

        } catch (Exception e){
            e.printStackTrace();
            log.error("导出机具列表异常", e);
        }
    }
}
