package cn.eeepay.boss.action;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.util.Constants;

/**
 * 系统用户管理
 * 
 * @author xyf1
 *
 */
@Controller
@RequestMapping(value = "/userAction")
public class UserAction {
	private static final String NEW_PASSWORD_WHEN_RESET = "abc888888";
	
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private TeamInfoService teamInfoService;
	@Resource
	private UserEntityInfoService userEntityInfoService;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private AgentInfoService agentInfoService;
	@Resource
	private PerAgentService perAgentService;
	@Resource
	private ShiroRoleService shiroRoleService;
	@Resource
	private ShiroRigthService shiroRigthService;
	@Resource
	private AccessService accessService;
	

	@RequestMapping("/selectByCondition.do")
	@ResponseBody
	public Object selectByCondition(@RequestBody JSONObject params) {
		UserInfo userInfo = new UserInfo();
		userInfo.setMobilephone(params.getString("mobilephone"));
		userInfo.setUserName(params.getString("userName"));
		userInfo.setStatus(params.getString("status"));
		userInfo.setHasChildren(params.getString("hasChildren"));
		List<UserEntityInfo> userEntityInfos = new ArrayList<>();
		UserLoginInfo userInfo1 = (UserLoginInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		userEntityInfos.add(userInfo1.getUserEntityInfo());
		userInfo.setUserEntityInfos(userEntityInfos);
		Sort sort = new Sort();
		Page<UserInfo> page = new Page<UserInfo>();
		sort.setSidx("id");

		processSortPage(params, sort, page);

		userInfoService.findUserInfoList(userInfo, sort, page,userInfo1.getUserEntityInfo().getEntityId());
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
		return page;
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

	@SystemLog(description = "新增用户")
	@RequestMapping("/addUser.do")
	@ResponseBody
	public Object addUser(@RequestBody JSONObject userInfoJson) {
		UserInfo userInfo = new UserInfo();

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
		userInfo.setId(userInfoJson.getInteger("id"));
		userInfo.setMobilephone(userInfoJson.getString("mobilephone"));
		userInfo.setPassword(userInfoJson.getString("password"));
		userInfo.setStatus(userInfoJson.getString("status"));
//		userInfo.setTeamId(userInfoJson.getString("teamId"));
//		userInfo.setTeamId(Constants.TEAM_ID);
		userInfo.setTeamId(info.getTeamId().toString());
		userInfo.setUserName(userInfoJson.getString("userName"));
		userInfo.setUserId(userInfoJson.getString("userId"));
		userInfo.setPassword(userInfoJson.getString("password"));
		userInfo.setCreateTime(new Date());
		userInfo.setUpdatePwdTime(new Date());
		userInfo.setEmail(userInfoJson.getString("email"));
	
		int appManager = "1".equals(userInfoJson.getString("appManager")) ? 1 : 0;
		
		JSONObject ret = new JSONObject();
		ret.put("status", false);
		if (!"1".equals(userInfo.getStatus())) {
			userInfo.setStatus("0");
		}
		final String userName = userInfo.getUserName();
		if (StringUtils.isBlank(userName)) {
			ret.put("msg", "请填写用户姓名");
			return ret;
		}
		if (userName.getBytes().length > 100) {
			ret.put("msg", "用户姓名太长");
			return ret;
		}
		final String mobilephone = userInfo.getMobilephone();
		if (StringUtils.isBlank(mobilephone)) {
			ret.put("msg", "请填写手机号");
			return ret;
		}
		if (!StringUtils.isNumeric(mobilephone) || mobilephone.length() != 11) {
			ret.put("msg", "请填写正确的手机号");
			return ret;
		}
		final String email = userInfo.getEmail();
		if (StringUtils.isBlank(email)) {
			ret.put("msg", "请填写邮箱");
			return ret;
		}
		 String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
		 Pattern regex = Pattern.compile(check);    
		 Matcher matcher = regex.matcher(email);    
		 if(!matcher.matches()){
			 ret.put("msg", "请填写正确的邮箱");
				return ret;
		 }
		final String password = userInfo.getPassword();
		if (StringUtils.isBlank(password)) {
			ret.put("msg", "请填写密码");
			return ret;
		}
		if (password.length() < 6 || password.getBytes().length > 20) {
			ret.put("msg", "请填写6~20位密码");
			return ret;
		}

		final String teamId = userInfo.getTeamId();
		TeamInfo teamInfo = teamInfoService.selectByTeamId(teamId);
		if (teamInfo == null) {
			ret.put("msg", "请填写正确的teamId");
			return ret;
		}
		
		
		final UserInfo foundUserInfo = userInfoService.findUserInfoByMobilePhoneAndTeamId(mobilephone, teamId);
		if(null != foundUserInfo){
			userInfo.setId(foundUserInfo.getId());
			final String userId = foundUserInfo.getUserId();
			userInfo.setUserId(userId);
			//判断是否已经存在user_entity_info数据
			if(null != userEntityInfoService.findAgentUserEntityInfoByUserId(userId)){
				ret.put("msg", "手机号已被使用，请更换手机号！");
				return ret;
			}
			// 没有user_entity_info数据，需要更新user_info数据，插入user_entity_info数据
		}
		
		Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
		userInfo.setPassword(passEnc.encodePassword(password, mobilephone));

		userInfoService.insertWithLoginUserEntityInfo(userInfo, appManager);

		//获取当前代理商可以分配的角色列表
		List<ShiroRole> shiroRoleList = shiroRoleService.findAllRoles(1);
		if(shiroRoleList == null){
			ret.put("status", false);
			ret.put("msg", "当前代理商不具有分配角色的权限");
			return ret;
		}
		JSONArray roleArr = userInfoJson.getJSONArray("roleIds");
		Set<String> roleSet = new HashSet<>();
		for(int i = 0; i < roleArr.size(); i++){
			String roleId = roleArr.getString(i);
			boolean flag = false;
			for (ShiroRole shiroRole : shiroRoleList) {
				if(roleId.equals(shiroRole.getId().toString())){
					flag = true;
					break;
				}
			}
			if(!flag){
				ret.put("status", false);
				ret.put("msg", "非法操作");
				return ret;
			}
			roleSet.add(roleId);
		}
		
		try {
			UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userInfo.getUserId());
			userRoleService.saveUserRole(ue.getId(), roleSet.toArray(new String[roleSet.size()]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ret.put("status", true);
		ret.put("msg", "添加成功");
		return ret;
	}

	@SystemLog(description = "修改用户")
	@RequestMapping("/updateUser.do")
	@ResponseBody
	public Object updateUser(@RequestBody JSONObject userInfoJson) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(userInfoJson.getInteger("id"));
		userInfo.setMobilephone(userInfoJson.getString("mobilephone"));
		userInfo.setPassword(userInfoJson.getString("password"));
		userInfo.setStatus(userInfoJson.getString("status"));
		userInfo.setTeamId(userInfoJson.getString("teamId"));
//		userInfo.setTeamId(Constants.TEAM_ID);
		userInfo.setUserName(userInfoJson.getString("userName"));
		userInfo.setUserId(userInfoJson.getString("userId"));
		userInfo.setEmail(userInfoJson.getString("email"));

		int appManager = "1".equals(userInfoJson.getString("manage")) ? 1 : 0;
		
		JSONObject ret = new JSONObject();
		ret.put("status", false);
		
//		UserLoginInfo userInfo1 = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userInfo.getUserId());
		if (ue == null || !accessService.canAccessTheAgent(ue.getEntityId(), false)) {
			ret.put("status", false);
			ret.put("msg", "无权操作");
			return ret;
		}
		if(StringUtils.isBlank(userInfo.getUserId())){
			ret.put("msg", "请填写用户ID");
			return ret;
		}
		if (!"1".equals(userInfo.getStatus())) {
			userInfo.setStatus("0");
		}
		final String userName = userInfo.getUserName();
		if (StringUtils.isBlank(userName)) {
			ret.put("msg", "请填写用户姓名");
			return ret;
		}
		if (userName.getBytes().length > 100) {
			ret.put("msg", "用户姓名太长");
			return ret;
		}
		final String mobilephone = userInfo.getMobilephone();
		if (StringUtils.isBlank(mobilephone)) {
			ret.put("msg", "请填写手机号");
			return ret;
		}
		final String email = userInfo.getEmail();
		//银行家,景灿要求去掉这个限制
//		if (StringUtils.isBlank(email)) {
//			ret.put("msg", "请填写邮箱");
//			return ret;
//		}
		if (!StringUtils.isNumeric(mobilephone) || mobilephone.length() != 11) {
			ret.put("msg", "请填写正确的手机号");
			return ret;
		}
		if (StringUtils.isNotBlank(email)) {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
			Pattern regex = Pattern.compile(check);    
			Matcher matcher = regex.matcher(email);    
			if(!matcher.matches()){
				ret.put("msg", "请填写正确的邮箱");
				return ret;
			}
		}
		Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
		final String password = userInfo.getPassword();
		if (StringUtils.isBlank(password)) {
			userInfo.setPassword(null);
		}else{
			if (password.length() < 6 || password.getBytes().length > 20) {
				ret.put("msg", "请填写6~20位密码");
				return ret;
			}
			userInfo.setPassword(passEnc.encodePassword(password, mobilephone));
		}
		final String teamId = userInfo.getTeamId();
		TeamInfo teamInfo = teamInfoService.selectByTeamId(teamId);
		if (teamInfo == null) {
			ret.put("msg", "请填写正确的teamId");
		}
		userInfoService.updateUserInfoByUserId(userInfo);
		
		UserEntityInfo ue2 = new UserEntityInfo();
		ue2.setUserId(userInfo.getUserId());
		ue2.setApply("1");
		ue2.setStatus(userInfo.getStatus());
		ue2.setManage(String.valueOf(appManager));
		userEntityInfoService.updateUserEntity(ue2);
		
		if (userInfoJson.containsKey("roleIds")) {
			JSONArray roleArr = userInfoJson.getJSONArray("roleIds");
			List<ShiroRole> shiroRoleList = shiroRoleService.findAllRoles(1);
			if(shiroRoleList == null){
				ret.put("status", false);
				ret.put("msg", "当前代理商不具有分配角色的权限");
				return ret;
			}
			Set<String> allCanUserRoleId = new HashSet<>();
			for (ShiroRole shiroRole : shiroRoleList) {
				allCanUserRoleId.add(shiroRole.getId() + "");
			}

			Set<String> roleSet = new HashSet<>();
			for (int i = 0; i < roleArr.size(); i++) {
				if (!allCanUserRoleId.contains(roleArr.getString(i))) {
					ret.put("status", false);
					ret.put("msg", "非法操作!");
					return ret;
				}
				roleSet.add(roleArr.getString(i));
			}
			try {
				userRoleService.saveUserRole(ue.getId(), roleSet.toArray(new String[roleSet.size()]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ret.put("status", true);
		ret.put("msg", "修改成功");
		return ret;
	}

	@SystemLog(description = "删除用户")
	@RequestMapping("/deleteUser.do")
	@ResponseBody
	public Object deleteUser(String userId) {
		JSONObject ret = new JSONObject();
		ret.put("status", false);
		
		if(StringUtils.isBlank(userId)){
			ret.put("msg", "请填写用户ID");
			return ret;
		}
		UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
		if(ue == null){
			ret.put("msg", "请填写正确的用户ID");
			return ret;
		}
		
//		UserLoginInfo userInfo1 = (UserLoginInfo) SecurityContextHolder.getContext()
//			    .getAuthentication()
//			    .getPrincipal();
//		if(!hasModifyPermit(userInfo1, ue)){
//			ret.put("msg", "无权限操作");
//			return ret;
//		}
		
		try {
			userInfoService.deleteUserInfoByUserId(userId);
			ret.put("msg", "删除成功");
			ret.put("status", true);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("msg", "系统错误 ，删除失败");
		}
		
		return ret;
	}
	
	@RequestMapping("getRolesByUserId.do")
	@ResponseBody
	public Object getRolesByUserId(String userId){
		try {
			UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
			if (ue == null || !accessService.canAccessTheAgent(ue.getEntityId(), false)) {
				return Collections.EMPTY_LIST;
			}
			//boolean adminCreateRole = shiroRigthService.isAdminCreateRole(ue.getId());
			if (ue.getIsAgent().equals("1")) {
				//List<UserRole> list = userRoleService.findUserRoleByUserIdNew(ue.getEntityId(),ue.getId());
				List<UserRole>	list = userRoleService.findUserRoleByOem(ue.getEntityId(),ue.getId());
				return list;
			}else {
				List<UserRole> list = userRoleService.findUserRoleByUserId(ue.getId());
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.EMPTY_LIST;
	}

	@SystemLog(description = "更新密码")
	@RequestMapping("updatePwd.do")
	@ResponseBody
	public Object updatePwd(@RequestBody JSONObject userInfoJson) {
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		JSONObject ret = new JSONObject();
		ret.put("status", false);
		ret.put("msg", "修改密码失败");
		String oldPwd = userInfoJson.getString("pwd");
		String newPwd = userInfoJson.getString("newPwd");

		UserLoginInfo userInfo1 = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserInfo userInfo = userInfoService.findUserInfoByMobilePhoneAndTeamId(userInfo1.getUsername(), userInfo1.getTeamId());
		Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
		
		if (!passEnc.encodePassword(oldPwd, userInfo1.getUsername()).equalsIgnoreCase(userInfo.getPassword())) {
			ret.put("msg", "原密码错误");
			return ret;
		}

		userInfoService.updateUserPwdByUserId(userInfo1.getUserEntityInfo().getUserId(),
				passEnc.encodePassword(newPwd, userInfo1.getUsername()));
		//同时更新对应的人人代理的密码
		if(Constants.PER_AGENT_TEAM_ID.equals(userInfo1.getTeamId())){
			perAgentService.updatePassword(loginAgent.getAgentNo(), passEnc.encodePassword(newPwd, userInfo1.getUsername()));
		}
		ret.put("status", true);
		ret.put("msg", "修改密码成功");
		return ret;
	}

	@SystemLog(description = "重置密码")
	@RequestMapping("resetPwd.do")
	@ResponseBody
	public Object resetPwd(@RequestParam("userId") String userId) {
		JSONObject ret = new JSONObject();

//		UserLoginInfo userInfo1 = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
		UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
		if (ue == null || !accessService.canAccessTheAgent(ue.getEntityId(), false)) {
			ret.put("status", false);
			ret.put("msg", "无权操作");
			return ret;
		}
		UserInfo userInfo = userInfoService.findUserInfoByUserId(userId);
//		UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
//		AgentInfo agent = agentInfoService.selectByagentNo(ue.getEntityId());
//		if (hasModifyPermit(userInfo1, ue) || agent.getParentId().equals(userInfo1.getUserEntityInfo().getEntityId())) {
		userInfoService.updateUserPwdByUserId(userId,
				passEnc.encodePassword(NEW_PASSWORD_WHEN_RESET, userInfo.getMobilephone()));
		ret.put("status", true);
		ret.put("msg", "重置密码成功为："+NEW_PASSWORD_WHEN_RESET);
//		} else {
//			ret.put("status", false);
//			ret.put("msg", "无权限操作");
//		}
		return ret;
	}

	/**
	 * 是否有权限重置密码
	 * admin拥有所有的权限，判断ALL	
	 * 目前是代理商可以管控自己和下级代理商（包括自己新建的用户），列表上也只展示了这些记录，所以后台不需要再做判断
	 * 这个判断只能管控代理商管理自己和自己新增的用户，所以暂时注释掉。
	 */
//	protected boolean hasModifyPermit(UserLoginInfo principle, UserEntityInfo userEntityInfo) {
//		return userEntityInfo != null && ("ALL".equals(principle.getUserEntityInfo().getEntityId())
//				|| userEntityInfo.getEntityId().equals(principle.getUserEntityInfo().getEntityId()));
//	}


	/**
	 * 修改用户的业务范围（组织id）
	 * @param user
	 */
	@RequestMapping("/changeAccessTeamId")
	@ResponseBody
	public Map<String, Object> changeAccessTeamId(@RequestBody UserInfo user){
		Map<String, Object> result = new HashMap<>();
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		UserEntityInfo userEntityInfo = userEntityInfoService.findAgentUserEntityInfoByUserId(user.getUserId());
		try {
			userInfoService.changeAccessTeamId(user.getUserId(), user.getTeamId());
			result.put("status", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", false);
			result.put("msg", "提交失败");
		}
		return result;
	}

	/**
	 * 获取当前登录代理商的业务范围
	 * @return
	 */
	@RequestMapping("/getAccessTeamId")
	@ResponseBody
	public Map<String, Object> getAccessTeamId(){
		Map<String, Object> result = new HashMap<>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String accessTeamId = principal.getUserEntityInfo().getAccessTeamId();
			if(StringUtils.isNotBlank(accessTeamId) && !"-1".equals(accessTeamId)){
				result.put("status", true);
				result.put("accessTeamId", accessTeamId);
			}else {
				result.put("status", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", false);
		}
		return result;
	}
}
