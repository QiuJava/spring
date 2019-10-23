package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.dao.SysConfigDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ShiroRigth;
import cn.eeepay.framework.model.TeamInfo;
import cn.eeepay.framework.model.UserEntityInfo;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.UserRigth;
import cn.eeepay.framework.model.UserRole;
import cn.eeepay.framework.service.AccessService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.AgentOperLogService;
import cn.eeepay.framework.service.PerAgentService;
import cn.eeepay.framework.service.ShiroRigthService;
import cn.eeepay.framework.service.ShiroRoleService;
import cn.eeepay.framework.service.TeamInfoService;
import cn.eeepay.framework.service.UserEntityInfoService;
import cn.eeepay.framework.service.UserInfoService;
import cn.eeepay.framework.service.UserRigthService;
import cn.eeepay.framework.service.UserRoleService;
import cn.eeepay.framework.service.impl.SuperBankWithdrawCheck;
import cn.eeepay.framework.util.OemTypeEnum;
import cn.eeepay.framework.util.Result;

/**
 * 9楼银行家剥离接口
 */
@Controller
@RequestMapping(value = "/superBankApi")
public class SuperBankApiAction {
	private static final String NEW_PASSWORD_WHEN_RESET = "abc888888";
	private static final Logger log = LoggerFactory.getLogger(SuperBankApiAction.class);
	
	@Resource
	private UserInfoService userInfoService;
	
	@Resource
	private ShiroRoleService shiroRoleService;
	
	@Resource
	private ShiroRigthService shiroRigthService;
	
	@Resource
	private UserRigthService userRigthService;
	
	@Resource
	private AgentInfoService agentInfoService;
	
	@Resource
	private TeamInfoService teamInfoService;
	
	@Resource
	private UserEntityInfoService userEntityInfoService;
	
	@Resource
	private UserRoleService userRoleService;
	
	@Resource
	private AccessService accessService;
	
	@Resource
	private PerAgentService perAgentService;
	
	@Resource
	private UserDetailsService userDetailsService;
	
	@Resource
	private AgentOperLogService agentOperLogService;
	
	@Resource
	private SuperBankWithdrawCheck superBankWithdrawCheck;
	
	@Resource
	private SysConfigDao sysConfigDao;
	
	/**
	 * 当前登录代理商信息
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/selectByPrincipal.do",method = RequestMethod.POST)
	@ResponseBody
	public Object selectByPrincipal(@RequestBody JSONObject params) {
		log.info("当前登录代理商selectByPrincipal  参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try{
	        //1.解析参数，md5加密校验，是否正常
	        if ( !checkSign(params, resultMap)){
	        	return resultMap;
	        }
	        //2.解析参数
	        String infoStr = params.getString("info");
	        JSONObject infoJson = JSONObject.parseObject(infoStr);
	        String agentNo = infoJson.getString("agentNo");//当前登录代理商编号,必传
	        if (StringUtils.isBlank(agentNo)) {
				resultMap.setMsg("代理商编号不能为空");
				return resultMap;
			}
	        AgentInfo loginAgent = agentInfoService.selectByPrincipalApi(agentNo);
	        if (loginAgent == null) {
	        	log.info("当前登录代理商编号参数 agentNo为 ===>" + agentNo + "查询当前登录代理商信息为空");
	        	resultMap.setMsg("代理商编号" + agentNo + "代理商信息为空");
				return resultMap;
			}
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("查询成功");
			resultMap.setData(loginAgent);
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			resultMap.setMsg("操作异常");
			log.error("操作异常",e);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}
	}

	/**
	 * 系统用户列表查询接口
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/selectByCondition.do",method = RequestMethod.POST)
	@ResponseBody
	public Object selectByCondition(@RequestBody JSONObject params) {
		log.info("系统用户列表查询   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try{
	        //1.解析参数，md5加密校验，是否正常
	        if ( !checkSign(params, resultMap)){
	        	return resultMap;
	        }
	        //2.解析参数
	        String infoStr = params.getString("info");
	        JSONObject infoJson = JSONObject.parseObject(infoStr);
	        String userId = infoJson.getString("userId");//当前登录代理商userId,必传
	        if (StringUtils.isBlank(userId)) {
				resultMap.setMsg("用户ID不能为空");
				return resultMap;
			}
			UserInfo userInfo = new UserInfo();
			userInfo.setMobilephone(infoJson.getString("mobilephone"));
			userInfo.setUserName(infoJson.getString("userName"));
			userInfo.setStatus(infoJson.getString("status"));
			userInfo.setHasChildren(infoJson.getString("hasChildren"));
			
			List<UserEntityInfo> userEntityInfos = new ArrayList<>();
	//		UserLoginInfo userInfo1 = (UserLoginInfo) SecurityContextHolder.getContext()
	//			    .getAuthentication()
	//			    .getPrincipal();
	//		userEntityInfos.add(userInfo1.getUserEntityInfo());
			UserEntityInfo userEntityInfo = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
			if(userEntityInfo == null){
				resultMap.setStatus(false);
				resultMap.setMsg("数据为空");
				log.info("根据userId === > " + userId + "在user_entity_info表中没查询到数据");
				return resultMap;
			}
			userEntityInfos.add(userEntityInfo);
			userInfo.setUserEntityInfos(userEntityInfos);
			Sort sort = new Sort();
			Page<UserInfo> page = new Page<UserInfo>();
			sort.setSidx("id");
	
			processSortPage(infoJson, sort, page);
	
			userInfoService.findUserInfoList(userInfo, sort, page,userEntityInfo.getEntityId());
			final List<UserInfo> result = page.getResult();
			if (result != null && !result.isEmpty()) {
				for (UserInfo item : result) {
					item.setPassword(null);
					item.setUpdatePwdTime(null);
					
					// 获取用户角色名称
					String roleName = shiroRoleService.getRoleName(item);
					item.setRoleName(roleName);
				}
			}
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("查询成功");
			resultMap.setData(page);
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			resultMap.setMsg("操作异常");
			log.error("操作异常",e);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/getRolesByUserId.do",method = RequestMethod.POST)
	@ResponseBody
	public Object getRolesByUserId(@RequestBody JSONObject params){
		log.info("getRolesByUserId.do   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
        //1.解析参数，md5加密校验，是否正常
        if ( !checkSign(params, resultMap)){
        	return resultMap;
        }
        //2.解析参数
        String infoStr = params.getString("info");
        JSONObject infoJson = JSONObject.parseObject(infoStr);
        String userId = infoJson.getString("userId");//当前登录代理商userId,必传
        if (StringUtils.isBlank(userId)) {
			resultMap.setMsg("用户ID不能为空");
			return resultMap;
		}
		try {
			UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
			if (ue == null) {
    			resultMap.setStatus(false);
    			resultMap.setMsg("数据为空");
    			log.info("根据userId === > " + userId + "在user_entity_info表中没查询到数据");
    			return resultMap;
    		}
			//boolean adminCreateRole = shiroRigthService.isAdminCreateRole(ue.getId());
			if (ue.getIsAgent().equals("1")) {
				//List<UserRole> list = userRoleService.findUserRoleByUserIdNew(ue.getEntityId(),ue.getId());
				List<UserRole>	list = userRoleService.findUserRoleByOem(ue.getEntityId(),ue.getId());
				resultMap.setStatus(true);
				resultMap.setCode(200);
				resultMap.setMsg("查询成功");
				resultMap.setData(list);
				log.info("返回数据:" + resultMap);
				return resultMap;
			}else {
				List<UserRole> list = userRoleService.findUserRoleByUserId(ue.getId());
				resultMap.setStatus(true);
				resultMap.setCode(200);
				resultMap.setMsg("查询成功");
				resultMap.setData(list);
				log.info("返回数据:" + resultMap);
				return resultMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.setData(Collections.EMPTY_LIST);
		return resultMap;
	}
	
	/**
	 * 系统用户 新增用户接口(只需保存用户信息，权限信息剥离系统这边保存自己的一套)
	 * @param userInfoJson
	 * @return
	 */
	@SystemLog(description = "新增用户")
	@RequestMapping(value = "/addUser.do",method = RequestMethod.POST)
	@ResponseBody
	public Object addUser(@RequestBody JSONObject params) {
		log.info("系统用户新增用户接口所传参数 params ==> " + params + "=====");
		String userId = null;
		Result resultMap = new Result();
		try {
	        //1.解析参数，md5加密校验，是否正常
	        if ( !checkSign(params, resultMap)){
	        	return resultMap;
	        }
	        //2.解析参数
	        String infoStr = params.getString("info");
	        JSONObject userInfoJson = JSONObject.parseObject(infoStr);
	        UserInfo userInfo = new UserInfo();
	//		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	//		AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
	        
	        String entityId = userInfoJson.getString("agentNo");//当前登录代理商编号
	        if (StringUtils.isBlank(entityId)) {
	        	resultMap.setMsg("参数有误");
	        	log.info("参数有误,所传当前登录代理商编号为 ===>" + entityId + "=====");
	        	return resultMap;
			}
	        if ("ALL".equals(entityId)) {
	        	resultMap.setMsg("admin账号不能新增,目前没这个功能");
	        	log.info("admin账号不能新增用户,目前没这个功能=====");
	        	return resultMap;
	        }
			AgentInfo info = agentInfoService.selectByagentNo(entityId);
			userInfo.setId(userInfoJson.getInteger("id"));
			userInfo.setMobilephone(userInfoJson.getString("mobilephone"));
			userInfo.setPassword(userInfoJson.getString("password"));
			userInfo.setStatus(userInfoJson.getString("status"));
			userInfo.setTeamId(info.getTeamId().toString());
			userInfo.setUserName(userInfoJson.getString("userName"));
			userInfo.setCreateTime(new Date());
			userInfo.setUpdatePwdTime(new Date());
			userInfo.setEmail(userInfoJson.getString("email"));
		
			int appManager = "1".equals(userInfoJson.getString("appManager")) ? 1 : 0;
			
			if (!"1".equals(userInfo.getStatus())) {
				userInfo.setStatus("0");
			}
			final String userName = userInfo.getUserName();
			if (StringUtils.isBlank(userName)) {
				resultMap.setMsg("请填写用户姓名");
				return resultMap;
			}
			if (userName.getBytes().length > 100) {
				resultMap.setMsg("用户姓名太长");
				return resultMap;
			}
			final String mobilephone = userInfo.getMobilephone();
			if (StringUtils.isBlank(mobilephone)) {
				resultMap.setMsg("请填写手机号");
				return resultMap;
			}
			if (!StringUtils.isNumeric(mobilephone) || mobilephone.length() != 11) {
				resultMap.setMsg("请填写正确的手机号");
				return resultMap;
			}
			final String email = userInfo.getEmail();
			if (StringUtils.isBlank(email)) {
				resultMap.setMsg("请填写邮箱");
				return resultMap;
			}
			 String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
			 Pattern regex = Pattern.compile(check);    
			 Matcher matcher = regex.matcher(email);    
			 if(!matcher.matches()){
				 resultMap.setMsg("请填写正确的邮箱");
				 return resultMap;
			 }
			final String password = userInfo.getPassword();
			if (StringUtils.isBlank(password)) {
				resultMap.setMsg("请填写密码");
				return resultMap;
			}
			if (password.length() < 6 || password.getBytes().length > 20) {
				resultMap.setMsg("请填写6~20位密码");
				return resultMap;
			}
			final String teamId = userInfo.getTeamId();
			TeamInfo teamInfo = teamInfoService.selectByTeamId(teamId);
			if (teamInfo == null) {
				resultMap.setMsg("请填写正确的teamId");
				return resultMap;
			}
			
			if(userInfoService.findUserInfoByEmailAndTeamId(email, teamId) != null){
				resultMap.setMsg("邮箱已被使用，请更换邮箱！");
				return resultMap;
			}
			final UserInfo foundUserInfo = userInfoService.findUserInfoByMobilePhoneAndTeamId(mobilephone, teamId);
			if(null != foundUserInfo){
				userInfo.setId(foundUserInfo.getId());
				userId = foundUserInfo.getUserId();
				userInfo.setUserId(userId);
				//判断是否已经存在user_entity_info数据
				if(null != userEntityInfoService.findAgentUserEntityInfoByUserId(userId)){
					resultMap.setMsg("手机号已被使用，请更换手机号！");
					return resultMap;
				}
				// 没有user_entity_info数据，需要更新user_info数据，插入user_entity_info数据
			}
			Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
			userInfo.setPassword(passEnc.encodePassword(password, mobilephone));
			userInfoService.insertWithLoginUserEntityInfoApi(userInfo,info, appManager);
//			UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userInfo.getUserId());
//			userId = ue.getId().toString();
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("添加成功");
			resultMap.setData(userInfo.getUserId());
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			resultMap.setMsg("操作异常");
			log.error("操作异常",e);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}
	}
	
	/**
	 * 系统用户 修改用户接口
	 * @param params
	 * @return
	 */
	@SystemLog(description = "修改用户")
	@RequestMapping(value = "/updateUser.do",method = RequestMethod.POST)
	@ResponseBody
	public Object updateUser(@RequestBody JSONObject params) {
		Result resultMap = new Result();
		try{
			log.info("系统用户修改用户接口所传参数参数 params ==> " + params + "=====");
	        //1.解析参数，md5加密校验，是否正常
	        if ( !checkSign(params, resultMap)){
	        	return resultMap;
	        }
	        //2.解析参数
	        String infoStr = params.getString("info");
	        JSONObject userInfoJson = JSONObject.parseObject(infoStr);
	        String userId = userInfoJson.getString("userId");//当前登录代理商userId, 必传
	        String agentNo = userInfoJson.getString("agentNo");//将被修改的用户对应的代理商编号 ,必传
	        String userIdForUpdate = userInfoJson.getString("userIdForUpdate");//将被修改的用户对应的userId ,必传
	        if (StringUtils.isBlank(userId) || StringUtils.isBlank(agentNo) || StringUtils.isBlank(userIdForUpdate)) {
	        	resultMap.setMsg("参数有误!");
	        	log.info("修改用户所传当前登录代理商userId 为 ===>" + userId + "====,将被修改的用户对应的代理商编号 agentNo ===>" + agentNo + "===,被修改的userId ===>" + userIdForUpdate + "===");
	        	return resultMap;
			}
			UserInfo userInfo = new UserInfo();
			userInfo.setId(userInfoJson.getInteger("id"));
			userInfo.setMobilephone(userInfoJson.getString("mobilephone"));
			userInfo.setPassword(userInfoJson.getString("password"));
			userInfo.setStatus(userInfoJson.getString("status"));
			userInfo.setTeamId(userInfoJson.getString("teamId"));
			userInfo.setUserName(userInfoJson.getString("userName"));
			userInfo.setUserId(userIdForUpdate);
			userInfo.setEmail(userInfoJson.getString("email"));
			int appManager = "1".equals(userInfoJson.getString("manage")) ? 1 : 0;
			
			UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
			if (ue == null) {
				resultMap.setStatus(false);
				resultMap.setMsg("数据为空");
				log.info("根据userId === > " + userId + "在user_entity_info表中没查询到数据");
				return resultMap;
			}
			String entityId = ue.getEntityId();
			if (!"ALL".equals(entityId) && (ue == null || ! accessService.canAccessTheAgentApi(entityId, agentNo, false))) {
				resultMap.setMsg("无权操作");
				return resultMap;
			}
			if(StringUtils.isBlank(userInfo.getUserId())){
				resultMap.setMsg("请填写用户ID");
				return resultMap;
			}
			if (!"1".equals(userInfo.getStatus())) {
				userInfo.setStatus("0");
			}
			final String userName = userInfo.getUserName();
			if (StringUtils.isBlank(userName)) {
				resultMap.setMsg("请填写用户姓名");
				return resultMap;
			}
			if (userName.getBytes().length > 100) {
				resultMap.setMsg("用户姓名太长");
				return resultMap;
			}
			final String mobilephone = userInfo.getMobilephone();
			if (StringUtils.isBlank(mobilephone)) {
				resultMap.setMsg("请填写手机号");
				return resultMap;
			}
			final String email = userInfo.getEmail();
			if (!StringUtils.isNumeric(mobilephone) || mobilephone.length() != 11) {
				resultMap.setMsg("请填写正确的手机号");
				return resultMap;
			}
			if (StringUtils.isNotBlank(email)) {
				String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
				Pattern regex = Pattern.compile(check);    
				Matcher matcher = regex.matcher(email);    
				if(!matcher.matches()){
					resultMap.setMsg("请填写正确的邮箱");
					return resultMap;
				}
			}
			Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
			final String password = userInfo.getPassword();
			if (StringUtils.isBlank(password)) {
				userInfo.setPassword(null);
			}else{
				if (password.length() < 6 || password.getBytes().length > 20) {
					resultMap.setMsg("请填写6~20位密码");
					return resultMap;
				}
				userInfo.setPassword(passEnc.encodePassword(password, mobilephone));
			}
			final String teamId = userInfo.getTeamId();
			TeamInfo teamInfo = teamInfoService.selectByTeamId(teamId);
			if (teamInfo == null) {
				resultMap.setMsg("请填写正确的teamId");
				return resultMap;
			}
			userInfoService.updateUserInfoByUserId(userInfo);
			
			UserEntityInfo ue2 = new UserEntityInfo();
			ue2.setUserId(userInfo.getUserId());
			ue2.setApply("1");
			ue2.setStatus(userInfo.getStatus());
			ue2.setManage(String.valueOf(appManager));
			userEntityInfoService.updateUserEntity(ue2);
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("修改成功");
			resultMap.setData(ue.getId());
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			resultMap.setMsg("操作异常");
			log.error("操作异常",e);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}
	}
	
	/**
	 * 系统用户   重置密码接口
	 * @param userId
	 * @return
	 */
	@SystemLog(description = "重置密码")
	@RequestMapping(value = "/resetPwd.do",method = RequestMethod.POST)
	@ResponseBody
	public Object resetPwd(@RequestBody JSONObject params) {
		log.info("系统用户   重置密码接口 参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try{
	        //1.解析参数，md5加密校验，是否正常
	        if ( !checkSign(params, resultMap)){
	        	return resultMap;
	        }
	        //2.解析参数
	        String infoStr = params.getString("info");
	        JSONObject userInfoJson = JSONObject.parseObject(infoStr);
	        String userId = userInfoJson.getString("userId");//当前登录代理商userId, 必传
	        String agentNo = userInfoJson.getString("agentNo");//将被重置密码用户对应的代理商编号 ,必传
	        if (StringUtils.isBlank(userId) || StringUtils.isBlank(agentNo)) {
	        	resultMap.setMsg("参数有误!");
	        	log.info("系统用户重置密码,所传参数 userId 为 ===>" + userId + "====,将被重置密码用户对应的代理商编号 agentNo ===>" + agentNo + "===");
	        	return resultMap;
			}
	
			Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
			UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
			if (ue == null || ! accessService.canAccessTheAgentApi(ue.getEntityId(),agentNo, false)) {
				resultMap.setMsg("无权操作");
				return resultMap;
				
			}
			UserInfo userInfo = userInfoService.findUserInfoByUserId(userId);
			userInfoService.updateUserPwdByUserId(userId,passEnc.encodePassword(NEW_PASSWORD_WHEN_RESET, userInfo.getMobilephone()));
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("重置密码成功为：" + NEW_PASSWORD_WHEN_RESET);
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			resultMap.setMsg("操作异常");
			log.error("操作异常",e);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}
		
	}
	
	/**
	 * 系统用户  修改密码接口
	 * @param userInfoJson
	 * @return
	 */
	@SystemLog(description = "更新密码")
	@RequestMapping(value = "/updatePwd.do",method = RequestMethod.POST)
	@ResponseBody
	public Object updatePwd(@RequestBody JSONObject params) {
		log.info("系统用户  修改密码接口 参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try{
	        //1.解析参数，md5加密校验，是否正常
	        if ( !checkSign(params, resultMap)){
	        	return resultMap;
	        }
	        //2.解析参数
	        String infoStr = params.getString("info");
	        JSONObject userInfoJson = JSONObject.parseObject(infoStr);
	        String userId = userInfoJson.getString("userId");//必传,用户ID
	        String oldPwd = userInfoJson.getString("pwd");
	        String newPwd = userInfoJson.getString("newPwd");
	        if (StringUtils.isBlank(userId) || StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)) {
	        	resultMap.setMsg("参数有误!");
	        	log.info("系统用户修改密码,所传参数 userId 为 ===>" + userId + "====");
	        	return resultMap;
			}
	        UserEntityInfo entityInfo = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
	        if (entityInfo == null) {
				resultMap.setStatus(false);
				resultMap.setMsg("数据为空");
				log.info("根据userId === > " + userId + "在user_entity_info表中没查询到数据");
				return resultMap;
			}
	//		UserLoginInfo userInfo1 = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserInfo userInfo1 = userInfoService.findUserInfoByUserId(userId);
			String mobilephone = userInfo1.getMobilephone();
			UserInfo userInfo = userInfoService.findUserInfoByMobilePhoneAndTeamId(mobilephone, userInfo1.getTeamId());
	//		Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
			if (!oldPwd.equalsIgnoreCase(userInfo.getPassword())) {
				resultMap.setMsg("原密码错误");
				log.info("返回数据:" + resultMap);
				return resultMap;
			}
	
			userInfoService.updateUserPwdByUserId(userId,newPwd);
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("修改密码成功");
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			resultMap.setMsg("操作异常");
			log.error("操作异常",e);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}
	}
	
	/**
	 * 发送验证码接口
	 * @param request
	 * @param response
	 * @return
	 */
	@SystemLog(description = "发送短信验证码")
    @RequestMapping(value = "/findPwd/sendMsgCode.do")
    @ResponseBody
    public Object sendMsgCode(@RequestBody JSONObject params) {
		log.info("发送短信验证码接口所传参数参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try{
	        //1.解析参数，md5加密校验，是否正常
	        if ( !checkSign(params, resultMap)){
	        	return resultMap;
	        }
	        //2.解析参数
	        String infoStr = params.getString("info");
	        JSONObject jsonObject = JSONObject.parseObject(infoStr);
	        String teamId = jsonObject.getString("teamId");//必传
	        String mobile = jsonObject.getString("mobile");//必传
	        if (StringUtils.isBlank(teamId) || StringUtils.isBlank(mobile)) {
	        	resultMap.setMsg("参数有误!");
	        	log.info("短信验证码接口所传参数 teamid为 ===>" + teamId + "==手机号为 ===>" + mobile + "====");
	        	return resultMap;
			}
	        UserInfo userInfo = userInfoService.findUserInfoByMobilePhoneAndTeamId(mobile, teamId);
	        if (userInfo == null) {
	        	resultMap.setMsg("根据手机号查询数据为空!");
	        	log.info("根据所传参数手机号 " + mobile + " + teamId " + teamId + " 在用户表没有查询到数据");
	        	return resultMap;
			}
	        resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setData(userInfo);
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			resultMap.setMsg("操作异常");
			log.error("操作异常",e);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}
    }
	
	/**
	 * 修改密码接口
	 * @param request
	 * @param response
	 * @return
	 */
	@SystemLog(description = "更新密码")
    @RequestMapping(value = "/findPwd/upPwd.do")
    @ResponseBody
    public Object upPwd(@RequestBody JSONObject params) {
		log.info("修改密码接口参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try{
	        //1.解析参数，md5加密校验，是否正常
	        if ( !checkSign(params, resultMap)){
	        	return resultMap;
	        }
	        //2.解析参数
	        String infoStr = params.getString("info");
	        JSONObject jsonObject = JSONObject.parseObject(infoStr);
	        String userId = jsonObject.getString("userId");//必传
	        String mobile = jsonObject.getString("mobile");//必传
	        String newPwd = jsonObject.getString("newPwd");//必传
	        String teamId = jsonObject.getString("teamId");//必传
	        if (StringUtils.isBlank(userId) || StringUtils.isBlank(mobile) || StringUtils.isBlank(newPwd) || StringUtils.isBlank(teamId)) {
	        	resultMap.setMsg("参数有误!");
	        	log.info("短信验证码接口所传参数 teamid为 ===>" + teamId + "==手机号为 ===>" + mobile + "===newPwd为 ==" + newPwd + " ===userId为===" + userId + "===");
	        	return resultMap;
			}
	        boolean status = false;
	        String body = "密码修改失败";
	        UserInfo userInfo = userInfoService.findUserInfoByUserId(userId);
	        if (userInfo != null) {
	            userInfo.setPassword(newPwd);
	            int row = userInfoService.updateUserInfoByUserId(userInfo);
	            if (row == 1) {
	                status = true;
	                body = "密码修改成功";
	                resultMap.setCode(200);
	            }
	        }
	        resultMap.setStatus(status);
			resultMap.setMsg(body);
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			resultMap.setMsg("操作异常");
			log.error("操作异常",e);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}
    }
	
	/**
	 * 根据用户名查询用户接口(期望：返回UserInfo 和 UserEntityInfo信息，权限部分剥离系统这边根据返回的信息自己处理)
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/loadUserByUsername.do",method = RequestMethod.POST)
	@ResponseBody
	public Object loadUserByUsername(@RequestBody JSONObject params){
		log.info("根据用户名查询用户接口loadUserByUsername  参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try{
	        //1.解析参数，md5加密校验，是否正常
	        if ( !checkSign(params, resultMap)){
	        	return resultMap;
	        }
	        //2.解析参数
	        String infoStr = params.getString("info");
	        JSONObject infoJson = JSONObject.parseObject(infoStr);
	        String userName = infoJson.getString("userName");//登录用户名,必传
	        String teamId = infoJson.getString("teamId");//组织ID,必传
	        String oemType = infoJson.getString("oemType");//ome类型,必传
	        if (StringUtils.isBlank(userName) || StringUtils.isBlank(teamId) || StringUtils.isBlank(oemType) || !"SUPERBANK".equals(oemType)) {
				resultMap.setMsg("参数错误");
				log.info("必传参数 userName ===> " + userName + ",teamId ===>" + teamId + ",oemType ===> " + oemType + "====");
				return resultMap;
			}
	        
	    	UserLoginInfo  userInfo = null;
	    	UserInfo userInfo2 = null;
	    	if(userName.contains("@")){
	    		userInfo2 = userInfoService.findUserInfoByEmailAndTeamId(userName, teamId);
	    	} else {
	    		userInfo2 = userInfoService.findUserInfoByMobilePhoneAndTeamId(userName, teamId);
	    	}
	    	if(userInfo2!=null && "0".equals(userInfo2.getStatus())){
				return null;
	//    		throw new RuntimeException("close");
	    	}
	
	    	if (userInfo2 != null) {
	    		UserEntityInfo currentUserEntityInfo = userEntityInfoService.findAgentUserEntityInfoByUserId(userInfo2.getUserId());
	    		if (currentUserEntityInfo == null) {
	    			resultMap.setStatus(false);
	    			resultMap.setMsg("数据为空");
	    			log.info("根据userId === > " + userInfo2.getUserId() + "在user_entity_info表中没查询到数据");
	    			return resultMap;
	    		}
	    		String agentNo = currentUserEntityInfo.getEntityId();
	    		if (org.springframework.util.StringUtils.hasLength(agentNo) && OemTypeEnum.THREE.getTransType().equals(oemType)) {
	    			// 不是一级代理商直接抛出用户名或密码错误
	        		boolean isOneAgent = agentInfoService.isOneAgent(agentNo);
	        		if (!isOneAgent) {
	        			resultMap.setMsg("用户名或密码错误");
	        			log.info("不是一级代理商直接返回用户名或密码错误");
	        			return resultMap;
	    			}
	    		}
	    		if (currentUserEntityInfo != null) {
		    		Integer uId = currentUserEntityInfo.getId();
		    		List<ShiroRigth> shiroRigths = shiroRigthService.findUserRolePrivilegeRigth(uId);//角色对应的权限
		    		
		    		Set<GrantedAuthority> authorities= new HashSet<GrantedAuthority>();
		    		List<UserRigth> userRigths = userRigthService.findUserRigthByUserId(uId);//用户对应的权限
					List<ShiroRigth> shiroRigths2 = new ArrayList<>();
					for (UserRigth userRigth : userRigths) {
						shiroRigths2.add(userRigth.getShiroRigth());
					}
					AgentInfo agentInfo = null;
					// 非admin创建的角色屏蔽掉系统管理菜单 
					//boolean isAdminCreateRole = shiroRigthService.isAdminCreateRole(uId);
					// 是代理商用户根据代理商类型和品牌 获取对应角色的权限
					if (currentUserEntityInfo.getIsAgent().equals("1") && !"ALL".equals(agentNo)) {
							List<ShiroRigth> oemRigthList = shiroRigthService.findRigthByOem(agentNo);
							if (oemRigthList.size()>0) {
								shiroRigths = oemRigthList;
							}
					}else if(currentUserEntityInfo.getIsAgent().equals("0")) {
						// 找到上级代理商
						 agentInfo = agentInfoService.getOneAgentByAgentNo(agentNo);
						 List<ShiroRigth> oemRigthList = shiroRigthService.findRigthByOem(agentInfo.getAgentNo());
						 // 权限过滤
						 shiroRigths = ListUtils.intersection(shiroRigths, oemRigthList);//交集
					}
					List<ShiroRigth> srs1 = ListUtils.intersection(shiroRigths, shiroRigths2);//交集
					List<ShiroRigth> srs2 = ListUtils.subtract(shiroRigths2, srs1);//相减
					
					//shiroRigths = ListUtils.subtract(shiroRigths, srs1);//相减
					List<ShiroRigth> srs = ListUtils.union(shiroRigths, srs2);
					srs = new ArrayList(new HashSet(srs));//去掉重复的用户真正的权限
	        		for (ShiroRigth sr : srs) {
	        			/*if (!isAdminCreateRole&& !currentUserEntityInfo.getEntityId().equals("ALL") && sr.getRigthCode().equals("sys") ) {
							continue;
						}*/
	        			authorities.add(new SimpleGrantedAuthority(sr.getRigthCode()));
					}
		        	userInfo = new UserLoginInfo(userInfo2.getMobilephone(), userInfo2.getPassword(), authorities);
	        		userInfo.setUserInfoId(userInfo2.getUserId());
					userInfo.setLockTime(userInfo2.getLockTime());
					userInfo.setWrongPasswordCount(userInfo2.getWrongPasswordCount());
		        	userInfo.setUserId(userInfo2.getId());
		        	userInfo.setRealName(userInfo2.getUserName());
		        	userInfo.setTelNo(userInfo2.getMobilephone());
		        	userInfo.setStatus(userInfo2.getStatus());
		        	userInfo.setUserEntityInfo(currentUserEntityInfo);
		        	userInfo.setSecondUserNode(userInfo2.getSecondUserNode());
		        	userInfo.setTeamId(userInfo2.getTeamId());
					userInfo.setOemTypeEnum(OemTypeEnum.getOemType(oemType));
	    		}
			}
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("查询成功");
			resultMap.setData(userInfo);
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			resultMap.setMsg("操作异常");
			log.error("操作异常",e);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}
	}
	
	/**
	 * 根据代理商编号查询全部代理商接口
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectAllInfo.do",method = RequestMethod.POST)
	public @ResponseBody Object selectAllInfo(@RequestBody JSONObject params){
		log.info("根据代理商编号查询全部代理商   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		List<AgentInfo> list = null;
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String agentNo = infoJson.getString("agentNo");//当前登录代理商编号 ,必传
			if (StringUtils.isBlank(agentNo)) {
				resultMap.setMsg("代理商编号不能为空");
				return resultMap;
			}
			list = agentInfoService.selectAllInfo(agentNo);
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("查询成功");
			resultMap.setData(list);
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			log.error("查询当前代理商下的所有代理商失败！",e);
			resultMap.setMsg("查询异常");
			return resultMap;
		}
	}
	
	/**
	 * 根据代理商编号查询代理商信息接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectByagentNo.do",method = RequestMethod.POST)
	@ResponseBody
	public Object selectByagentNo(@RequestBody JSONObject params) {
		AgentInfo info=null;
		log.info("根据代理商编号查询代理商信息  参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String agentNo = infoJson.getString("agentNo");//所要查询信息的代理商编号
			if (StringUtils.isBlank(agentNo)) {
				resultMap.setMsg("代理商编号不能为空");
				return resultMap;
			}
			info = agentInfoService.selectByagentNo(agentNo);
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("查询成功");
			resultMap.setData(info);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}catch(Exception e){
			log.error("查询代理商异常！",e);
			resultMap.setMsg("查询代理商异常！");
			return resultMap;
		}
	}
	
	/**
	 * 获取我的账户信息 - 账户服务信息部分的接口(其他部分剥离系统可以直接请求账户系统接口获取)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMyAccount.do",method = RequestMethod.POST)
	@ResponseBody
	public Object getMyAccount(@RequestBody JSONObject params) {
		log.info("获取我的账户信息   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		Map<String, Object> map = new HashMap<>();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String agentNo = infoJson.getString("agentNo");//当前登录代理商编号,必传
			if (StringUtils.isBlank(agentNo)) {
				resultMap.setMsg("代理商编号不能为空");
				return resultMap;
			}
			map = agentInfoService.getMyAccount(agentNo);
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("查询成功");
			resultMap.setData(map);
			log.info("返回数据:" + resultMap);
			return resultMap;
		}catch(Exception e){
			log.error("查询异常！",e);
			resultMap.setMsg("查询异常！");
			return resultMap;
		}
	}
	
	/**
	 * 聚合平台分润账户提现接口
	 * @param params
	 * @return
	 */
	@SystemLog(description = "超级银行家分润提现")
	@RequestMapping(value="/takeReplayBalanceSuperBank.do",method = RequestMethod.POST)
	@ResponseBody
	public Object takeReplayBalanceSuperBank(@RequestBody JSONObject params){
		log.info("获取我的账户信息   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String agentNo = infoJson.getString("agentNo");//当前登录代理商编号,必传
			String userId = infoJson.getString("userId");//当前登录代理商userId, 必传
			String money = infoJson.getString("money");//提现金额,必传
			if (StringUtils.isBlank(agentNo)) {
				resultMap.setMsg("代理商编号不能为空");
				return resultMap;
			}
			if (StringUtils.isBlank(money)) {
				resultMap.setMsg("提现金额不能为空");
				return resultMap;
			}
			boolean commonWithdrawCash = agentInfoService.commonWithdrawCashApi(money, "224116", "11",agentNo,userId, superBankWithdrawCheck);
			if (commonWithdrawCash) {
				resultMap.setStatus(true);
				resultMap.setCode(200);
				resultMap.setMsg("提现提交成功");
				log.info("返回数据:" + resultMap);
				return resultMap;
			}
			resultMap.setMsg("提现提交失败");
			return resultMap;
		} catch (Exception e) {
			log.error("超级银行家余额提现提交异常",e);
			resultMap.setMsg(e.getMessage());
			return resultMap;
		}
	}
	
	/**
	 * 根据代理商提现服务ID查询到费率接口
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/getSingleNumAmount.do",method = RequestMethod.POST)
	@ResponseBody
	public Object getSingleNumAmount(@RequestBody JSONObject params){
		log.info("根据提现服务ID查询费率接口   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String serviceId = infoJson.getString("serviceId");//服务ID,必传
			if (StringUtils.isBlank(serviceId)) {
				resultMap.setMsg("提现服务ID不能为空");
				return resultMap;
			}
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("查询成功");
			resultMap.setData(agentInfoService.getSingleNumAmount(serviceId));
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			log.error("提现服务查询异常",e);
			resultMap.setMsg("查询异常");
			return resultMap;
		}
	}
	
	/**
	 * 查询是否是外放平台接口
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/selectIsOpen.do",method = RequestMethod.POST)
	@ResponseBody
	public Object selectIsOpen(@RequestBody JSONObject params){
		log.info("查询是否是外放平台   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String agnentNo = infoJson.getString("agnentNo");//当前登录代理商编号,必传
			if (StringUtils.isBlank(agnentNo)) {
				resultMap.setMsg("代理商编号不能为空");
				return resultMap;
			}
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("查询成功");
			resultMap.setData(agentInfoService.selectIsOpen(agnentNo));
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			log.error("查询异常",e);
			resultMap.setMsg("查询异常");
			return resultMap;
		}
	}
	
	/**
	 * 根据userId查询用户信息
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/findAgentUserEntityInfoByUserId.do",method = RequestMethod.POST)
	@ResponseBody
	public Object findAgentUserEntityInfoByUserId(@RequestBody JSONObject params){
		log.info("findAgentUserEntityInfoByUserId.do    参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String userId = infoJson.getString("userId");//必传
			if (StringUtils.isBlank(userId)) {
				resultMap.setMsg("用户ID不能为空");
				return resultMap;
			}
			UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
			if (ue == null) {
				resultMap.setStatus(false);
				resultMap.setMsg("数据为空");
				log.info("根据userId === > " + userId + "在user_entity_info表中没查询到数据");
				return resultMap;
			}
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("查询成功");
			resultMap.setData(ue.getId());
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			log.error("查询异常",e);
			resultMap.setMsg("查询异常");
			return resultMap;
		}
	}
	
	/**
	 * 清除用户登录错误次数
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/clearWrongPasswordCount.do",method = RequestMethod.POST)
	@ResponseBody
	public Object clearWrongPasswordCount(@RequestBody JSONObject params){
		log.info("清除用户登录错误次数接口   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String userId = infoJson.getString("userId");//必传
			if (StringUtils.isBlank(userId)) {
				resultMap.setMsg("用户ID不能为空");
				return resultMap;
			}
			userInfoService.clearWrongPasswordCount(userId);
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("操作成功");
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			log.error("清除用户登录错误次数出现异常",e);
			resultMap.setMsg("操作异常");
			return resultMap;
		}
	}
	/**
	 * 增加用户登录次数
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/increaseWrongPasswordCount.do",method = RequestMethod.POST)
	@ResponseBody
	public Object increaseWrongPasswordCount(@RequestBody JSONObject params){
		log.info("增加用户登录次数接口   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String userId = infoJson.getString("userId");//必传
			if (StringUtils.isBlank(userId)) {
				resultMap.setMsg("用户ID不能为空");
				return resultMap;
			}
			userInfoService.increaseWrongPasswordCount(userId);
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("操作成功");
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			log.error("增加用户登录次数出现异常",e);
			resultMap.setMsg("操作异常");
			return resultMap;
		}
	}
	/**
	 * 锁定用户接口
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/lockLoginUser.do",method = RequestMethod.POST)
	@ResponseBody
	public Object lockLoginUser(@RequestBody JSONObject params){
		log.info("锁定用户接口   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String userId = infoJson.getString("userId");//必传
			if (StringUtils.isBlank(userId)) {
				resultMap.setMsg("用户ID不能为空");
				return resultMap;
			}
			userInfoService.lockLoginUser(userId);
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("操作成功");
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			log.error("锁定用户出现异常",e);
			resultMap.setMsg("操作异常");
			return resultMap;
		}
	}
	/**
	 * 登录用户名或密码错误限制次数
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/queryWrongPasswordMaxCount.do",method = RequestMethod.POST)
	@ResponseBody
	public Object queryWrongPasswordMaxCount(@RequestBody JSONObject params){
		log.info("锁定用户接口   参数 params ==> " + params + "=====");
		Result resultMap = new Result();
		try {
			//1.解析参数，md5加密校验，是否正常
			if ( !checkSign(params, resultMap)){
				return resultMap;
			}
			//2.解析参数
			String infoStr = params.getString("info");
			JSONObject infoJson = JSONObject.parseObject(infoStr);
			String sysKey = infoJson.getString("sysKey");//必传 agent_web_login_wrong_password_max_count 取次数,agent_web_login_lock_time 取锁定时间
			resultMap.setStatus(true);
			resultMap.setCode(200);
			resultMap.setMsg("操作成功");
			resultMap.setData(sysConfigDao.getStringValueByKey(sysKey));
			log.info("返回数据:" + resultMap);
			return resultMap;
		} catch (Exception e) {
			log.error("锁定用户出现异常",e);
			resultMap.setMsg("操作异常");
			return resultMap;
		}
	}
	
	public static void processSortPage(JSONObject params, Sort sort, Page<?> page) {
		final String sortdir = params.getString("sortdir");
		final String sortstr = params.getString("sort");
		if (StringUtils.isNotBlank(sortstr)) {
			sort.setSidx(sortstr);
			if ("desc".equalsIgnoreCase(sortdir)) {
				sort.setSord(Sort.DESC);
			} else {
				sort.setSord(Sort.ASC);
			}
		}
		final String pageStr = params.getString("page");
		if (StringUtils.isNumeric(pageStr)) {
			int pageInt = Integer.parseInt(pageStr);
			if (pageInt < 1)
				pageInt = 1;
			page.setPageNo(pageInt);
		}
		final String pageSizeStr = params.getString("pageSize");
		if (StringUtils.isNumeric(pageSizeStr)) {
			int pageSizeInt = Integer.parseInt(pageSizeStr);
			if (pageSizeInt < 1)
				pageSizeInt = 1;
			page.setPageSize(pageSizeInt);
		}
	}
	
	/**
     * 校验请求签名
     * @param paramJson
     * @param result
     * @return
     */
    private boolean checkSign(JSONObject paramJson, Result result) {
        String info = paramJson.getString("info");
        String signParam = paramJson.getString("sign");
        if(StringUtils.isBlank(info) || StringUtils.isBlank(signParam)){
            result.setMsg("参数异常");
            return false;
        }

        String superBankApiKey = "TeENiFAOo4Qku1jKuk5x";
        String sign = Md5.md5Str(info + superBankApiKey);
        if(StringUtils.isBlank(signParam) || !signParam.equals(sign)){
            result.setCode(403);
            result.setMsg("请求非法");
            return false;
        }
        return true;
    }
}
