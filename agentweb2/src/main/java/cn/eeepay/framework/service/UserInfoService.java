package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.UserInfo;

public interface UserInfoService {
	int selectUserNoSeq();
	int insert(UserInfo userInfo);
	int insertWithLoginUserEntityInfo(UserInfo userInfo, int appManager);
	int insertWithLoginUserEntityInfoApi(UserInfo userInfo,AgentInfo entityInfo, int appManager);
	int updateUserInfoByUserId(UserInfo userInfo);
	int deleteUserInfoById(Integer id);
	int deleteUserInfoByUserId(String userId) throws Exception;
	int updateUserPwdByUserId(String userId,String password);
	List<UserInfo> findUserInfoList(UserInfo user,Sort sort,Page<UserInfo> page, String loginAgentNo);
	List<UserInfo> findAllUserInfo();
	UserInfo findUsersWithRoleByMobilePhone(String mobilePhone);
	UserInfo findUserInfoByMobilePhoneAndTeamId(String mobilePhone,String teamId);
	UserInfo findUserInfoByUserId(String userId);
	boolean isBlocked(String key);
	UserInfo selectInfoByTelNo(String telNo, String teamId);
	int updateInfoByMp(String telNo, String newTelNo, String teamId);
	UserInfo findUserInfoByEmailAndTeamId(String userName, String teamId);

    void changeAccessTeamId(String userId, String teamId);
    /**
     * 清除用户登录错误次数
     * @param userId
     */
    void clearWrongPasswordCount(String userId);
    /**
     * 增加用户登录次数
     * @param userId
     */
	void increaseWrongPasswordCount(String userId);
	/**
	 * 多次登录用户名或密码错误 ,锁定用户
	 * @param userId
	 */
	void lockLoginUser(String userId);
}
