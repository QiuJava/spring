package cn.eeepay.framework.service;

import cn.eeepay.framework.model.UserEntityInfo;

public interface UserEntityInfoService {
	int insertUserEntity(UserEntityInfo userEntityInfo);
	int updateUserEntity(UserEntityInfo userEntityInfo);
	UserEntityInfo findAgentUserEntityInfoWithRoleByUserId(String userId);
	UserEntityInfo findAgentUserEntityInfoByUserId(String userId);
	int deleteAgentUserEntityById(Integer id);
}
