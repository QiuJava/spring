package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.UserInfoDao;
import cn.eeepay.framework.dao.UserInfoReadDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.UserEntityInfo;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
	@Resource
	public UserInfoDao userDao;
	@Resource
	private UserInfoReadDao userInfoReadDao;
	@Resource
	private RedisService redisService;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private UserRigthService userRigthService;
	@Resource
	private UserEntityInfoService userEntityInfoService;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private AgentInfoDao agentInfoDao;

	
	@Resource
	private AgentInfoService agentInfoService;
	
	private final int MAX_ATTEMPT = 5;//登录最大错误次数

	@Override
	public boolean isBlocked(String key) {
		try {
			int num = 0;
			if (redisService.exists(key)) {
				num = Integer.valueOf(redisService.select(key).toString());
			}
            return num >= MAX_ATTEMPT;
        } catch (Exception e) {
            return false;
        }
	}
	@Override
	public List<UserInfo> findAllUserInfo() {
		return userDao.findAllUserInfo();
	}
	@Override
	public int insert(UserInfo userInfo) {
		if(StringUtils.isBlank(userInfo.getUserId())){
			userInfo.setUserId(String.format("1%018d", selectUserNoSeq()));
		}
		return userDao.insert(userInfo);
	}
	@Override
	public int selectUserNoSeq() {
		return userDao.selectUserNoSeq();
	}
	@Override
	public int updateUserInfoByUserId(UserInfo userInfo) {
		return userDao.updateUserInfoByUserId(userInfo);
	}
	@Override
	public int deleteUserInfoById(Integer id) {
		return userDao.deleteUserInfoById(id);
	}
	@Override
	public int deleteUserInfoByUserId(String userId) throws Exception {
		UserEntityInfo ue = userEntityInfoService.findAgentUserEntityInfoByUserId(userId);
		userRoleService.deleteUserRoleByUserId(ue.getId());
		int i = userEntityInfoService.deleteAgentUserEntityById(ue.getId());
		i += userDao.deleteUserInfoByUserId(userId);
		return i;
	}
	@Override
	public int updateUserPwdByUserId(String userId, String password) {
		return userDao.updateUserPwdByUserId(userId, password);
	}
	@Override
	public List<UserInfo> findUserInfoList(UserInfo user, Sort sort, Page<UserInfo> page, String loginAgentNo) {
//		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String entityId = principal.getUserEntityInfo().getEntityId();
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(loginAgentNo);
		String loginAgentNode = agentInfo != null ? agentInfo.getAgentNode() : "";
		String order = null;
		if (StringUtils.isNotBlank(propertyMapping(sort.getSidx()))) {
			order = String.format("order by %s %s", propertyMapping(sort.getSidx()), sort.getSord());

		}
		if ("ALL".equals(loginAgentNo)) {
			return userInfoReadDao.findUserInfoListByAdmin(user, order, page);
		}else {
			AgentInfo info = agentInfoService.selectByagentNo(loginAgentNo);
			return userInfoReadDao.findUserInfoListByAgent(user, info.getTeamId(), order, loginAgentNode, page);
		}
	}
	public String propertyMapping(String name){
		Map<String, String> map = new HashMap<>();
		map.put("id", "id");
		map.put("userId", "user_id");
		map.put("userName", "user_name");
		map.put("password", "password");
		map.put("status", "status");
		map.put("mobilephone", "mobilephone");
		map.put("createTime", "create_time");
		map.put("teamId", "team_id");
		return map.get(name);
	}

	@Override
	public UserInfo findUsersWithRoleByMobilePhone(String mobilePhone) {
		return userDao.findUsersWithRoleByMobilePhone(mobilePhone);
	}
	@Override
	public UserInfo findUserInfoByUserId(String userId) {
		return userDao.findUserInfoByUserId(userId);
	}
	@Override
	public UserInfo findUserInfoByMobilePhoneAndTeamId(String mobilePhone, String teamId) {
		return userDao.findUserInfoByMobilePhoneAndTeamId(mobilePhone, teamId);
	}
	@Override
	public int insertWithLoginUserEntityInfo(UserInfo userInfo, int appManager) {
		int i;
		if(userInfo.getId() == null){
			if(StringUtils.isBlank(userInfo.getUserId())){
				userInfo.setUserId(String.format("1%018d", selectUserNoSeq()));
			}
			i = userDao.insert(userInfo);
		}else{
			// 只是没有user_entity_info数据，需要更新user_info数据，插入user_entity_info数据
			i=userDao.updateUserInfoByUserId(userInfo);
		}
		UserLoginInfo userInfo1 = (UserLoginInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		UserEntityInfo userEntityInfo = new UserEntityInfo();
		userEntityInfo.setUserId(userInfo.getUserId());
		userEntityInfo.setUserType("1");
		userEntityInfo.setApply("1");
		userEntityInfo.setManage(String.valueOf(appManager));
		userEntityInfo.setStatus("1");
		userEntityInfo.setEntityId(userInfo1.getUserEntityInfo().getEntityId());
		i+= userEntityInfoService.insertUserEntity(userEntityInfo);
		return i;
	}
	@Override
	public int insertWithLoginUserEntityInfoApi(UserInfo userInfo,AgentInfo entityInfo, int appManager) {
		int i;
		if(userInfo.getId() == null){
			if(StringUtils.isBlank(userInfo.getUserId())){
				userInfo.setUserId(String.format("1%018d", selectUserNoSeq()));
			}
			i = userDao.insert(userInfo);
		}else{
			// 只是没有user_entity_info数据，需要更新user_info数据，插入user_entity_info数据
			i=userDao.updateUserInfoByUserId(userInfo);
		}
		UserEntityInfo userEntityInfo = new UserEntityInfo();
		userEntityInfo.setUserId(userInfo.getUserId());
		userEntityInfo.setUserType("1");
		userEntityInfo.setApply("1");
		userEntityInfo.setManage(String.valueOf(appManager));
		userEntityInfo.setStatus("1");
		userEntityInfo.setEntityId(entityInfo.getAgentNo());
		i+= userEntityInfoService.insertUserEntity(userEntityInfo);
		return i;
	}
	@Override
	public UserInfo findUserInfoByEmailAndTeamId(String email, String teamId) {
		return userDao.findUserInfoByEmailAndTeamId(email, teamId);
	}
	
	@Override
	public UserInfo selectInfoByTelNo(String telNo, String teamId) {
		return userDao.selectInfoByTelNo(telNo, teamId);
	}

	@Override
	public int updateInfoByMp(String telNo, String newTelNo, String teamId) {
		Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
		String pwd=passEnc.encodePassword(sysDictService.selectRestPwd().getSysValue(), newTelNo);
		return userDao.updateInfoByMp(telNo, newTelNo, teamId, pwd);
	}

	@Override
	public void changeAccessTeamId(String userId, String teamId) {
		userDao.changeAccessTeamId(userId, teamId);
	}
	
	@Override
	public void clearWrongPasswordCount(String userId) {
		userDao.clearWrongPasswordCount(userId);
	}
	
	@Override
	public void increaseWrongPasswordCount(String userId) {
		userDao.increaseWrongPasswordCount(userId);
	}
	
	@Override
	public void lockLoginUser(String userId) {
		userDao.lockLoginUser(userId);
	}
}
