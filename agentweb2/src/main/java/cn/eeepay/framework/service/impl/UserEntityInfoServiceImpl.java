package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.UserEntityInfoDao;
import cn.eeepay.framework.model.UserEntityInfo;
import cn.eeepay.framework.service.UserEntityInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service("userEntityInfoService")
public class UserEntityInfoServiceImpl implements UserEntityInfoService {
	@Resource
	public UserEntityInfoDao userEntityInfoDao;

	@Override
	public int insertUserEntity(UserEntityInfo userEntityInfo) {
		return userEntityInfoDao.insertUserEntity(userEntityInfo);
	}

	@Override
	public int updateUserEntity(UserEntityInfo userEntityInfo) {
		return userEntityInfoDao.updateUserEntity(userEntityInfo);
	}

	@Override
	public UserEntityInfo findAgentUserEntityInfoWithRoleByUserId(String userId) {
		return userEntityInfoDao.findAgentUserEntityInfoWithRoleByUserId(userId);
	}

	@Override
	public UserEntityInfo findAgentUserEntityInfoByUserId(String userId) {
		return userEntityInfoDao.findAgentUserEntityInfoByUserId(userId);
	}

	@Override
	public int deleteAgentUserEntityById(Integer id) {
		return userEntityInfoDao.deleteAgentUserEntityById(id);
	}
	

}
