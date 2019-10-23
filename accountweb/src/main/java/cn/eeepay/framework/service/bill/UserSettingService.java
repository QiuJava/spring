package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.model.bill.UserSetting;

public interface UserSettingService {
	void init()  throws Exception;
	int insert(UserSetting userSetting) throws Exception;
	int update(UserSetting userSetting) throws Exception;
	int deleteUserSettingByUserId(Integer userId) throws Exception;
	UserSetting findUserSettingByUserId(Integer userId) throws Exception;
	List<UserSetting> findAllUserSetting() throws Exception;
}
