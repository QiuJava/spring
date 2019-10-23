package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping(value = "/agentInfo")
public class AgentInfoAction {


	private static final Logger log = LoggerFactory.getLogger(AgentInfoAction.class);
	@Resource
	public AgentInfoService agentInfoService;
	
	@Resource
	public AgentShareTaskService agentShareTaskService;
	@Resource
	private PerAgentService perAgentService;
	@Resource
	private TerminalInfoService terminalInfoService;

	@Resource
	private AccessService accessService;

	@RequestMapping(value = "/selectHappyBackList")
	public @ResponseBody Result selectHappyBackList() throws Exception {
		Result result = new Result();
		try {
			Map<String, List<String>> activityTypeNoAndTeamIdMap = agentInfoService.getActivityTypeNoAndTeamIdMap(agentInfoService.getCurAgentNo());
			List<AgentActivity> agentActivities = agentInfoService.selectHappyBackList();
			Map<String, Object> data = new HashMap<>();
			data.put("activityTypeNoAndTeamIdMap", activityTypeNoAndTeamIdMap);
			data.put("agentActivities", agentActivities);
			result.setData(data);
			result.setStatus(true);
		} catch (Exception e) {
			result.setMsg("查询欢乐返异常!");
			result.setStatus(false);
			log.error("新增代理商时,查询欢乐返活动异常！");
		}
		return result;
	}

	@RequestMapping(value = "/selectChildAgentByAgentNo.do")
	public @ResponseBody Map<String, Object> selectChildAgentByAgentNo(@RequestParam(value="agentNo", required=true)String agentNo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AgentInfo> list = null;
		try {
			AgentInfo cu = agentInfoService.selectByagentNo(agentNo);
			//只查询所有下级代理商，不包括自己
			list = agentInfoService.selectChildAgentByAgentNode(cu.getAgentNode(),cu.getAgentLevel()+1);
			map.put("agentList", list);
		} catch (Exception e) {
			log.error("根据代理商id查询所有子代理商失败！",e);
		}
		return map;
	}

	@RequestMapping(value = "/selectAllInfo")
	public @ResponseBody Object selectAllInfo() throws Exception {
		List<AgentInfo> list = null;
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			list = agentInfoService.selectAllInfo(principal.getUserEntityInfo().getEntityId());
		} catch (Exception e) {
			log.error("查询当前代理商下的所有代理商失败！");
		}
		return list;
	}


	@RequestMapping(value = "/selectAllInfoBelong")
	public @ResponseBody Object selectAllInfoBelong() {
		List<Map<String, Object>> list = null;
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			list = agentInfoService.selectAllInfoBelong(principal.getUserEntityInfo().getEntityId());
		} catch (Exception e) {
			log.error("查询当前代理商下的所有代理商失败！",e);
		}
		return list;
	}

	@RequestMapping(value = "/selectSelfAndDirectChildren")
	public @ResponseBody List<AgentInfo> selectSelfAndDirectChildren() throws Exception {
		List<AgentInfo> list = null;
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			list = agentInfoService.selectSelfAndDirectChildren(principal.getUserEntityInfo().getEntityId());
		} catch (Exception e) {
			log.error("查询当前代理商以及直接下级代理商异常！");
		}
		return list;
	}

	@RequestMapping(value = "/selectDirectChildren")
	public @ResponseBody List<AgentInfo> selectDirectChildren() throws Exception {
		List<AgentInfo> list = null;
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			list = agentInfoService.selectDirectChildren(principal.getUserEntityInfo().getEntityId());
		} catch (Exception e) {
			log.error("查询当前代理商以及直接下级代理商异常！");
		}
		return list;
	}
	
	@RequestMapping(value = "/selectAllInfoByTeamId")
	public @ResponseBody Object selectAllInfoByTeamId(String teamId) throws Exception {
		List<AgentInfo> list = null;
		try {
			list = agentInfoService.selectAllInfo(teamId);
		} catch (Exception e) {
			log.error("查询组织ID所有代理商失败！");
		}
		return list;
	}
	
	/**
	 * 查询所有的业务产品对应的服务费率和服务额度
	 * @param
	 */
	@RequestMapping(value = "/getAgentServices")
	@ResponseBody
	public Map<String,Object> getAgentServices(@RequestBody String params) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {
			List<Integer> json = JSONArray.parseArray(params,Integer.class);
			map.put("agentId", "0");
			map.put("bpIds",json);
			map = agentInfoService.getAgentServices(map);
		} catch (Exception e) {
			log.error("查询代理商的服务信息失败！");
		}
		return map;
	}

    @Resource
    private PosCardBinService posCardBinService;
    /**
     *  走清算中心，验证身份证、开户名、银行卡号
     *  @param params
     *  @return
     *  @author zengja
     *  @date 2014年11月21日 上午11:53:59
     */
//    private Map<String, String> checkBank_Name_IDCard(Map<String, String> params) {
//        log.info("验证身份证、开户名、银行卡：" + params);
//        String ip = "http://www.yfbpay.cn/boss/api/checkMain";
//        //logger.info("数据库中配置的身份证、开户名、银行卡验证接口地址："+ip);
//        StringBuffer url = new StringBuffer(ip);
//        url.append("?");
//        String responseBody = null;
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("errCode", "faild");
//        map.put("errMsg", "开户名+账号+证件号码,校验失败");
//        map.put("exceptionMsg", "--");
//        try {
//            //(1:验证账号+户名  2：验证账号+户名+证件号)
//            String verifyType = params.get("verifyType");
//            verifyType = (StringUtils.isEmpty(verifyType)) ? "2" : verifyType;
//            url.append("verifyType=").append(verifyType);
//
//            //标识为AGENT 代理商系统访问接口
//            String channel = params.get("channel");
//            channel = (StringUtils.isEmpty(channel)) ? "AGENT" : channel;
//            url.append("&channel=").append(channel);
//
//            //身份证号码
//            String identityId = params.get("idcard");
//            url.append("&identityId=").append(identityId);
//
//            //银行卡号
//            String accNo = params.get("account_no");
//            url.append("&accNo=").append(URLEncoder.encode(accNo, "UTF-8"));
//
//            //开户名称
//            String accName = params.get("account_name");
//            url.append("&accName=").append(URLEncoder.encode(accName, "UTF-8"));
//
//            //清算联行号
//            String bankNo = params.get("cnaps_no");
//            if (StringUtils.isEmpty(bankNo)) { //如果APP没有传入清算联行号，则从 CardBin 中取
//                try {
//                    String bankNoTemp = posCardBinService.queryBankNo(accNo);
//                    if (bankNoTemp.equals("0") || bankNoTemp.equals("1")) {
//                        bankNo = null;
//                    } else {
//                        bankNo = bankNoTemp;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            url.append("&bankNo=").append(bankNo);
//            String finalUrl = url.toString();
//            log.info("验证身份证、开户名、银行卡，最终URL：" + finalUrl);
//            HttpClient client = HttpClientBuilder.create()
//                    .setConnectionTimeToLive(10000, TimeUnit.SECONDS)
//                    .build();
//            HttpGet method = new HttpGet(finalUrl);
//            HttpResponse execute = client.execute(method);
//            responseBody = IOUtils.toString(execute.getEntity().getContent());
//            String errCode = responseBody.substring(responseBody.indexOf("<errCode>") + 9, responseBody.indexOf("</errCode>"));
//            String errMsg = responseBody.substring(responseBody.indexOf("<errMsg>") + 8, responseBody.indexOf("</errMsg>"));
//            map.put("errCode", errCode);
//            map.put("errMsg", errMsg);
//            // 释放连接
//            method.releaseConnection();
//            // response.sendRedirect(url.toString());
//        } catch (ConnectTimeoutException cte) {
//            log.info("验证身份证、开户名、银行卡：连接超时<ConnectTimeoutException>");
//            map.put("exceptionMsg", "连接超时");
//            cte.printStackTrace();
//        } catch (SocketTimeoutException ste) {
//            log.info("验证身份证、开户名、银行卡：读取超时<SocketTimeoutException>");
//            map.put("exceptionMsg", "读取超时");
//            ste.printStackTrace();
//        } catch (IOException e) {
//            log.info("验证身份证、开户名、银行卡：其他异常<IOException>");
//            e.printStackTrace();
//        }
//        return map;
//    }
	/**
	 * 保存添加的代理商
	 * @param {agentInfo:代理商基本信息,dpData:业务产品id数组,
	 * 			shareData:分润信息,rateData:费率信息,quotaData:额度信息}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveAgentInfo")
	@ResponseBody
	@SystemLog(description = "新增代理商")
	public Map<String,Object> saveAgentInfo(@RequestBody String data) throws Exception {
		Map<String,Object> result = new HashMap<>();
		try {
			log.info("saveAgentInfo:" + data);
			JSONObject json=JSONObject.parseObject(data);

            agentInfoService.saveAgentInfo(json);
			result.put("status", true);
		}catch (AgentWebException e){
			log.error("保存代理商信息异常！" + e.getMessage());
			result.put("status", false);
			result.put("msg", e.getMessage());
		}catch (Exception e) {
			log.error("保存代理商信息异常！",e);
			result.put("status", false);
			String str=e.getMessage();
			if(StringUtils.isBlank(str)){
				result.put("msg", "代理商信息不完整");
				return result;
			}
			if(str.contains("\r\n")||str.contains("\n"))
				result.put("msg", "保存代理商信息异常");
			else
				result.put("msg", str);
		}
		return result;
	}
	@RequestMapping(value = "/selectByActivityTypeNo")
	@ResponseBody
	public Map<String,Object> selectByActivityTypeNo(@RequestParam String activityTypeNo) throws Exception {
		Map<String,Object> msg = new HashMap<>();
		try{
			msg = agentInfoService.selectByActivityTypeNo(activityTypeNo);
		}catch(Exception e){
			log.error("查询下发返现金额和税额百分比异常！",e);
		}
		return msg;
	}
	@RequestMapping(value = "/queryAgentInfoList")
	@ResponseBody
	public Map<String,Object> queryAgentInfoList(@RequestParam("baseInfo")String data,@Param("page")Page<AgentInfo> page) throws Exception {
		Map<String,Object> msg = new HashMap<>();
		try{
			@SuppressWarnings("unchecked")
			Map<String,Object> params=JSONObject.parseObject(data, Map.class);
			msg = agentInfoService.queryAgentInfoList(params,page);
		}catch(Exception e){
			log.error("查询代理商异常！",e);
		}
		return msg;
	}
	
	/**
	 * 查询代理商的基本信息
	 */
	@RequestMapping(value = "/queryAgentInfo")
	@ResponseBody
	public AgentInfo queryAgentInfo(@RequestBody String agentNo) throws Exception {
		AgentInfo info=null;
		try{
			JSONObject json=JSONObject.parseObject(agentNo);
			if(StringUtils.isNotBlank(json.getString("agentNo"))){
				info=agentInfoService.selectByagentNo(json.getString("agentNo"));
			}	
		}catch(Exception e){
			log.error("查询代理商异常！",e);
		}
		return info;
	}
	
	/**
	 * 通过代理商下所有的业务产品 
	 * @param
	 */
	@RequestMapping(value = "/queryAgentProducts")
	@ResponseBody
	public Map<String,Object> queryAgentProducts(@RequestBody String param) throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			String agentNo=json.getString("agentNo");
			Integer teamId=json.getInteger("teamId");
			if(StringUtils.isNotBlank(agentNo)&&teamId!=null){
				map =agentInfoService.queryAgentProducts(agentNo,teamId);
				map.put("status", true);
			}else{
				map.put("status", true);
				map.put("msg", "没有查询到代理商业务产品！");
			}
		} catch (Exception e) {
			map.put("status", false);
			map.put("msg", "代理商关联查询业务产品失败！");
			log.error("代理商关联查询业务产品失败！",e);
		}
		return map;
	}
	
	/**
	 * 通过代理商下详情信息
	 */
	@RequestMapping(value = "/queryAgentInfoDetail")
	@ResponseBody
	public Map<String,Object> queryAgentInfoDetail(@RequestBody String param) throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			String agentNo=json.getString("agentNo");
			Integer teamId=json.getInteger("teamId");
			if(StringUtils.isBlank(agentNo) || teamId == null){
				throw new AgentWebException("没有查询到代理商业务产品");
			}
			map =agentInfoService.queryAgentInfoDetail(agentNo,teamId);
			map.put("status", true);
		}catch (AgentWebException e){
			map.put("status", false);
			map.put("msg", e.getMessage());
			log.error("查询代理商详情失败",e);
		}catch (Exception e) {
			map.put("status", false);
			map.put("msg", "代理商关联查询业务产品失败！");
			log.error("代理商关联查询业务产品失败！",e);
		}
		return map;
	}
	
	/**
	 * 代理的业务产品查询出所有的服务的分润信息
	 * @param dbIds<Ingeter> 业务产品ID
	 */
	@RequestMapping(value = "/getAgentShareInfos")
	@ResponseBody
	public List<AgentShareRule> getAgentShareInfos(@RequestBody String dbIds) throws Exception {
		List<AgentShareRule> list = new ArrayList<>();
		try {
			List<Integer> json= JSONArray.parseArray(dbIds,Integer.class);
//			list = agentInfoService.getAgentShareInfos(json);	
		} catch (Exception e) {
			log.error("代理商关联查询业务产品失败！");
		}
		return list;
	}
	
	/**
	 * 删除代理商
	 */
	@RequestMapping(value = "/delAgent")
	@ResponseBody
	@SystemLog(description = "删除代理商")
	public Map<String,Object> delAgent(@RequestBody String agentNo) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		/*
		 * if (!accessService.canAccessTheAgent(agentNo, true)) { msg.put("status",
		 * false); msg.put("msg", "无权操作"); return msg; }
		 * 
		 * try { String str= agentInfoService.delAgent(agentNo); msg.put("status",
		 * true); msg.put("msg", str); } catch (Exception e) { log.error("删除代理商异常！",e);
		 * msg.put("status", false); msg.put("msg", "删除代理商异常"); }
		 */
		
		msg.put("status", false);
		msg.put("msg", "暂不支持删除代理商操作");
		return msg;
	}
	
	/**
	 * 通过代理商下详情用于修改
	 */
	@RequestMapping(value = "/editAgentInfoDetail")
	@ResponseBody
	public Map<String,Object> editAgentInfoDetail(@RequestBody String param) throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			String agentNo=json.getString("agentNo");
			Integer teamId=json.getInteger("teamId");
			if(StringUtils.isNotBlank(agentNo)&&teamId!=null){
				map =agentInfoService.queryAgentInfoDetail(agentNo,teamId,true);
				map.put("status", true);
			}else{
				map.put("status", true);
				map.put("msg", "没有查询到代理商业务产品！");  
			}
		}catch (AgentWebException e){
			map.put("status", false);
			map.put("msg", e.getMessage());
			log.error("查询代理商详情失败！",e);
		}catch (Exception e) {
			map.put("status", false);
			map.put("msg", "代理商关联查询业务产品失败！");
			log.error("代理商关联查询业务产品失败！",e);
		}
		return map;
	}


	/**
	 * 开启/关闭代理商业务产品
	 */
	@SystemLog(description = "开启/关闭代理商业务产品")
	@RequestMapping(value = "/updateAgentProStatus")
	@ResponseBody
	public ResponseBean updateAgentProStatus(String agentNo, String bpId, String status) throws Exception {
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			return new ResponseBean(agentInfoService.updateAgentProStatus(entityId, agentNo, bpId, status));
		}catch (Exception e){
			return new ResponseBean(e);
		}
	}
	/**
	 * 获取新的服务
	 * @throws Exception
	 */
	@RequestMapping(value = "/getNewAgentServices")
	@ResponseBody
	@Deprecated
	public Map<String,Object> getNewAgentServices(@RequestBody String params) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {
			map = agentInfoService.getNewAgentServices(JSONObject.parseObject(params,Map.class));
		} catch (Exception e) {
			log.error("查询代理商的服务信息失败！",e);
		}
		return map;
	}

	@RequestMapping("/getNewAgentServicesByBpId")
	@ResponseBody
	public ResponseBean getNewAgentServicesByBpId(@RequestBody List<String> bpIds){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			return new ResponseBean(agentInfoService.getNewAgentServicesByBpId(bpIds, entityId));
		}catch (Exception e){
			return new ResponseBean(e);
		}
	}

	/**
	 * 添加新分润信息任务列表
	 */
	@RequestMapping(value = "/addNewShare")
	@ResponseBody
	@SystemLog(description = "新增代理商分润")
	public Map<String,Object> addNewShare(@RequestBody String params) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {
			log.info("addNewShare: " + params);
			AgentShareRuleTask share = JSONObject.parseObject(params,AgentShareRuleTask.class);
			Date nowDate = new Date();
			//如果填写的生效日期小于当前日期，则返回
			if(share.getEfficientDate().getTime() < (nowDate.getTime() + 5*60*1000)){
				map.put("status", false);
				map.put("msg", "新增分润失败,生效日期必须大于等于当前日期！");
				return map;
			}
			int count=agentShareTaskService.insertAgentShareList(share);
			if(count>0){
				map.put("status", true);
				map.put("msg", "添加成功！");
			}else{
				map.put("status", false);
				map.put("msg", "添加失败！");
			}
		}catch (AgentWebException e){
			log.error(e.getMessage());
			map.put("status", false);
			map.put("msg", e.getMessage());
		}catch (Exception e) {
			log.error("添加新的分润信息！",e);
			map.put("status", false);
			map.put("msg", "添加失败");
		}
		return map;
	}
	/**
	 * 删除新分润信息任务列表
	 */
	@RequestMapping(value = "/delNewShare")
	@ResponseBody
	@SystemLog(description = "删除代理商分润")
	public Map<String,Object> delNewShare(@RequestBody Integer id) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {
			int count=agentShareTaskService.deleteAgentShareTask(id);
			if(count>0){
				map.put("status", true);
			}else{
				map.put("status", false);
				map.put("msg", "删除新的分润信息失败！");
			}
		} catch (Exception e) {
			log.error("查询代理商的服务信息失败！",e);
			map.put("status", false);
			map.put("msg", "系统异常！删除新的分润信息失败！");
		}
		return map;
	}
	/**
	 * 查询分润信息任务列表
	 */
	@RequestMapping(value = "/queryNewShareList")
	@ResponseBody
	public List<AgentShareRuleTask> queryNewShareList(@RequestBody Integer id) throws Exception {
		List<AgentShareRuleTask> list=new ArrayList<>();
		try {
			list=agentShareTaskService.getAgentShareRuleTask(id);
			
		} catch (Exception e) {
			log.error("查询分润信息任务列表异常！",e);
		}
		return list;
	}
	
	/**
	 * 保存代理商修改
	 */
	@RequestMapping(value = "/updateAgent")
	@ResponseBody
	@SystemLog(description = "更新代理商")
	public Map<String,Object> updateAgent(@RequestBody String data, HttpServletRequest request) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		try {
			log.info("updateAgent:" + data);
			JSONObject json=JSONObject.parseObject(data);

			AgentInfo agent=JSONObject.parseObject(json.getString("agentInfo"), AgentInfo.class);
			//			Map<String, String> paramsMap = new HashMap<>();
//			if (agent.getAccountType() != null && agent.getAccountType().equals(2)){
//				paramsMap.put("verifyType", "2");
//				paramsMap.put("account_no", agent.getAccountNo());		//银行卡号
//				paramsMap.put("account_name", agent.getAccountName());				//开户名
//				paramsMap.put("idcard", agent.getIdentityId());				//身份证
//				paramsMap.put("cnaps_no",agent.getCnapsNo() + "");			//银联卡号
//				Map<String, String> stringStringMap = checkBank_Name_IDCard(paramsMap);
//				if (StringUtils.equalsIgnoreCase(stringStringMap.get("errCode"), "faild")){
//					msg.put("status", false);
//					msg.put("msg", "实名验证失败.请检查银行卡号,开户名,身份证,开户行是否正确.");
//					return msg;
//				}
//			}
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//			agent.setTeamId(Integer.valueOf(Constants.TEAM_ID));
			agent.setTeamId(info.getTeamId());
			
			
			//旧的代理商
			AgentInfo oldInfo = agentInfoService.selectByagentNo(agent.getAgentNo());
			String updateType = Objects.toString(json.get("updateType"), "default");
			if (StringUtils.equalsIgnoreCase(updateType, "default")) {
				String safePhone = oldInfo.getSafephone();
				if(StringUtils.isNotBlank(safePhone)){
					safePhone = safePhone.substring(0, 3) + "*****" + safePhone.substring(8, safePhone.length());
				}
				//卡号或者开户名被修改
				if(!agent.getAccountName().equals(oldInfo.getAccountName())
						|| !agent.getAccountNo().equals(oldInfo.getAccountNo())
						|| !agent.getAccountType().equals(oldInfo.getAccountType())
						|| !agent.getCnapsNo().equals(oldInfo.getCnapsNo())
						|| !agent.getAccountProvince().equals(oldInfo.getAccountProvince())){
					msg.put("status", false);
					msg.put("msg", "safephone");
					msg.put("safephone", safePhone);
					return msg;
				}
			}else if (StringUtils.equalsIgnoreCase(updateType, "setSafePhone")) {
				CheckNum check=JSONObject.parseObject(json.getString("info"), CheckNum.class);
				agent.setTeamId(info.getTeamId());
				//校验短信及代理商编号及时间是否过期
				HttpSession session = request.getSession();
				String oldphoneNumLevel = (String)session.getAttribute("oldphoneNumLevel");
				String oldphoneNumTimeLevel =(String)session.getAttribute("oldphoneNumTimeLevel");

				if(StringUtils.isBlank(oldphoneNumLevel) || StringUtils.isBlank(oldphoneNumTimeLevel)){
					msg.put("msg", "请重新获取验证码");
					return msg;
				}

				String[] split = oldphoneNumLevel.split("-");
				String agentNo =  split[0];
				String checkNum =  split[1];

				if(!agent.getAgentNo().equals(agentNo)){
					msg.put("msg", "不对应的代理商编号");
					return msg;
				}
				if(!check.getPhoneNum().equals(checkNum)){
					msg.put("msg", "短信验证码有误");
					return msg;
				}
				if(Long.valueOf(oldphoneNumTimeLevel)-System.currentTimeMillis()<0){
					msg.put("msg", "验证码超过5分钟,请重新获取");
					return msg;
				}
				session.removeAttribute("oldphoneNumLevel");
				session.removeAttribute("oldphoneNumTimeLevel");
			}

			String str=agentInfoService.updateAgent(data);
			msg.put("status", true);
			msg.put("msg", str);
		}catch (AgentWebException e){
			log.error(e.getMessage());
			msg.put("msg", e.getMessage());
			msg.put("status", false);
		}catch (Exception e) {
			log.error(e.getMessage());
			log.error("保存代理商修改异常！",e);
			msg.put("status", false);
			msg.put("msg", "保存代理商修改异常");
		}
		return msg;
	}

	/**
	 * 重置密码tgh227
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/restPwd.do")
	@ResponseBody
	@SystemLog(description = "重置代理商密码")
	public Map<String, Object> updateRestPwd(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSON.parseObject(param);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());

			String paramAgentNo = json.getString("agentNo");
			AgentInfo modifyAgentInfo = agentInfoService.selectByMobilephoneAndTeamId(json.getString("mobilephone"), info.getTeamId().toString());
			if (modifyAgentInfo == null || !accessService.canAccessTheAgent(modifyAgentInfo.getAgentNo(), false) ||
				!StringUtils.equalsIgnoreCase(modifyAgentInfo.getAgentNo(), paramAgentNo)) {
				msg.put("msg", "无权操作");
				return msg;
			}

			int i =agentInfoService.updateRestPwd(json.getString("mobilephone"),info.getTeamId().toString());
			if(i>0){
				msg.put("msg", "重置密码成功:abc888888");
				if(998 == info.getTeamId()){
					//同步代理商对应的人人代理账号的密码
					String agentNo = json.getString("agentNo");
					Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
					String password = passEnc.encodePassword("abc888888", json.getString("mobilephone"));
					int j = perAgentService.updatePassword(agentNo, password);
					if(j > 0){
						msg.put("msg", "代理商和人人代理APP重置密码成功:abc888888");
					}else {
						msg.put("msg", "代理商重置密码成功:abc888888，人人代理APP重置密码失败");
					}
				}
			}else{
				msg.put("msg", "重置密码失败");
			}
		} catch (Exception e) {
			log.error("重置密码失败");
			msg.put("msg", "重置密码失败");
		}
		return msg;
	}

	@RequestMapping("/updateProfitSwitch")
	@ResponseBody
	@SystemLog(description = "修改代理商分润功能")
	public ResponseBean updateProfitSwitch(@RequestBody AgentInfo agentInfo){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			agentInfoService.updateProfitSwitch(entityId, agentInfo.getAgentNo(), agentInfo.getProfitSwitch());
		}catch (Exception e){
			return new ResponseBean(e);
		}
		return new ResponseBean("更新分润日结功能成功.", true);
	}

	@RequestMapping("/updatePromotionSwitch")
	@ResponseBody
	@SystemLog(description = "修改代理商推广功能")
	public ResponseBean updatePromotionSwitch(@RequestBody AgentInfo agentInfo){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			agentInfoService.updatePromotionSwitch(entityId, agentInfo.getAgentNo(), agentInfo.getPromotionSwitch());
		}catch (Exception e){
			return new ResponseBean(e);
		}
		return new ResponseBean("更新代理商推广功能成功.", true);
	}

	@RequestMapping("/updateCashBackSwitch")
	@ResponseBody
	@SystemLog(description = "修改代理商返现开关")
	public ResponseBean updateCashBackSwitch(@RequestBody AgentInfo agentInfo){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			agentInfoService.updateCashBackSwitch(entityId, agentInfo.getAgentNo(), agentInfo.getCashBackSwitch());
		}catch (Exception e){
			return new ResponseBean(e);
		}
		return new ResponseBean("更新代理商欢乐返返现功能成功.", true);
	}
	@RequestMapping("/updateFullPrizeSwitch")
	@ResponseBody
	@SystemLog(description = "修改代理商满奖开关")
	public ResponseBean updateFullPrizeSwitch(@RequestBody AgentInfo agentInfo){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			agentInfoService.updateFullPrizeSwitch(entityId, agentInfo.getAgentNo(), agentInfo.getFullPrizeSwitch());
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseBean(e);
		}
		return new ResponseBean("更新代理商满奖功能成功.", true);
	}
	@RequestMapping("/updateNotFullDeductSwitch")
	@ResponseBody
	@SystemLog(description = "修改代理商不满扣开关")
	public ResponseBean updateNotFullDeductSwitch(@RequestBody AgentInfo agentInfo){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			agentInfoService.updateNotFullDeductSwitch(entityId, agentInfo.getAgentNo(), agentInfo.getNotFullDeductSwitch());
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseBean(e);
		}
		return new ResponseBean("更新代理商欢乐返不满扣功能成功.", true);
	}

	@RequestMapping("/batchUpdateProfitSwitch")
	@ResponseBody
	@SystemLog(description = "批量修改代理商分润功能")
	public ResponseBean batchUpdateProfitSwitch(@RequestBody Map<String, Object> param){
		try {
			log.info("请求参数 {}", param);
			List<String> agentNos = (List<String>)param.get("agentNo");
			Integer profitSwitch = (Integer)param.get("profitSwitch");
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			agentInfoService.batchUpdateProfitSwitch(entityId, agentNos, profitSwitch);
		}catch (Exception e){
			return new ResponseBean(e);
		}
		return new ResponseBean("更新分润日结功能成功.", true);
	}

	@SystemLog(description = "批量修改代理商推广开关")
    @RequestMapping("/batchUpdatePromotionSwitch")
    @ResponseBody
    public ResponseBean batchUpdatePromotionSwitch(@RequestBody Map<String, Object> param){
        try {
			log.info("请求参数 {}", param);
            List<String> agentNos = (List<String>)param.get("agentNo");
            Integer promotionSwitch = (Integer)param.get("promotionSwitch");
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String entityId = principal.getUserEntityInfo().getEntityId();
            agentInfoService.batchUpdatePromotionSwitch(entityId, agentNos, promotionSwitch);
        }catch (Exception e){
            return new ResponseBean(e);
        }
        return new ResponseBean("更新代理商推广功能成功.", true);
    }

	@RequestMapping("/batchUpdateCashBackSwitch")
	@ResponseBody
	@SystemLog(description = "批量修改代理商返现开关")
	public ResponseBean batchUpdateCashBackSwitch(@RequestBody Map<String, Object> param){
		try {
			log.info("请求参数 {}", param);
			List<String> agentNos = (List<String>)param.get("agentNo");
			Integer cashBackSwitch = (Integer)param.get("cashBackSwitch");
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			agentInfoService.batchUpdateCashBackSwitch(entityId, agentNos, cashBackSwitch);
		}catch (Exception e){
			return new ResponseBean(e);
		}
		return new ResponseBean("更新代理商欢乐返返现功能成功.", true);
	}

    @RequestMapping("/getLoginAgentInfo")
	@ResponseBody
	public ResponseBean getLoginAgentInfo(){
    	try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			return new ResponseBean(agentInfoService.selectByagentNo(entityId));
		}catch (Exception e){
    		return new ResponseBean(e);
		}
	}
    
    @RequestMapping(value = "/selectConfigInfo")
	public @ResponseBody Object selectConfigInfo() throws Exception {
		List<AgentInfo> list = null;
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			list = agentInfoService.getConfigInfo(principal.getUserEntityInfo().getEntityId());
			//list = agentInfoService.getConfigInfo("1441");
		} catch (Exception e) {
			log.error("查询被授权查询的代理商失败！");
		}
		return list;
	}

    @RequestMapping(value = "/getAgentShareList",method = RequestMethod.GET)
	public @ResponseBody Object getAgentShareList(){
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", false);
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			if (agentShareTaskService.getAgentShareList(entityId).size() > 0){
				resultMap.put("status", true);
				return resultMap;
			}
			resultMap.put("msg", "请先联系上级代理为您配置正确的分润及活动参数");
			return resultMap;
		} catch (Exception e) {
			log.error("分润查询异常！",e);
			resultMap.put("msg", "分润查询失败");
			return resultMap;
		}
	}



    @RequestMapping(value = "/chidrenAgent")
    @ResponseBody
   	public Result chidrenAgent() throws Exception {
    	Result result = new Result();
   		 UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   		try {

   			List<AgentInfo> selectDirectChildren = agentInfoService.selectSelfAndDirectChildren(principal.getUserEntityInfo().getEntityId());
   			AgentInfo agentInfo = new  AgentInfo();
   			agentInfo.setAgentName("全部");
   			agentInfo.setAgentNo("");
   			selectDirectChildren.add(agentInfo);
   			result.setData(selectDirectChildren);
   			result.setStatus(true);
   		} catch (Exception e) {
   			result.setStatus(false);
   			result.setMsg("查询失败");
   			log.error("查询失败");
   		}
   		return result;
   	}

	/**
	 * 查询当前代理商的一级代理商勾选的欢乐返子类型
	 * @param
	 */
	@RequestMapping(value = "/getAgentActivity")
	@ResponseBody
	public Map<String,Object> getAgentActivity() throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
			List<AgentActivity>  list=agentInfoService.getAgentActivity();
			map.put("status", true);
			map.put("list", list);
		} catch (Exception e) {
			map.put("status", false);
			map.put("msg", "查询当前代理商的一级代理商勾选的欢乐返子类型失败！");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 开启、关闭欢乐返活动
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/switchHappyBack")
	@ResponseBody
	public Map<String, Object> switchHappyBack(Long id, boolean status,String agentNode, String activityTypeNo){
		Map<String, Object> map = new HashMap<>();
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		if(!agentNode.startsWith(loginAgent.getAgentNode())){
			map.put("status", false);
			map.put("msg", "非法操作");
		}
		try {
			if(status){// 开启
				// 判断该欢乐返子类型是否允许代理商更改
				int updateAgentStatus = agentInfoService.selectUpdateAgentStatusByActivityTypeNo(activityTypeNo);
				if(updateAgentStatus == 0){
					map.put("status", false);
					map.put("msg", "该活动暂不支持自定义更改");
					return map;
				}
				// 判断当前登录的代理商（上级）的该活动是否打开
				AgentActivity activity = agentInfoService.selectAgentActivityByAgentNoAndTypeNo(loginAgent.getAgentNo(), activityTypeNo);
				if(!activity.isStatus()){
					map.put("status", false);
					map.put("msg", "暂不支持开启");
					return map;
				}
				agentInfoService.updateAgentActivityStatus(id, status);
			}else {
				agentInfoService.updateAgentActivityStatusByAgentNode(agentNode, status, activityTypeNo);
			}
			map.put("status", true);
		} catch (Exception e) {
			log.error("更改欢乐返活动状态失败");
			map.put("status", false);
		}
		return map;
	}
}
