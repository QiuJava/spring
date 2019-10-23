package cn.eeepay.framework.service.bill.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.UserSettingMapper;
import cn.eeepay.framework.model.bill.ShiroUser;
import cn.eeepay.framework.model.bill.UserSetting;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.ShiroUserService;
import cn.eeepay.framework.service.bill.UserSettingService;
import cn.eeepay.framework.util.Constants;

@Service("userSettingService")
@Transactional
public class UserSettingServiceImpl implements UserSettingService {
	@Resource
	public UserSettingMapper userSettingMapper;
	@Resource
	private RedisService redisService;
	@Resource
	private ShiroUserService shiroUserService;
	
	@PostConstruct
	@Override
	public void init() throws Exception {
		List<UserSetting> userSettingList= userSettingMapper.findAllUserSetting();
		List<ShiroUser> shiroUserList = shiroUserService.findAllUsers();
		List<String> keys = new ArrayList<String>();
		for (ShiroUser user : shiroUserList) {
			String key = Constants.user_setting_list_redis_key +":" + user.getId();
			if (redisService.exists(key)) {
				keys.add(key);
			}
		}
		redisService.delete(keys);
		
		for (UserSetting us : userSettingList) {
			String key = Constants.user_setting_list_redis_key +":" + us.getUserId();
			redisService.insertString(key, us);
		}
		System.out.println("userSettingService init");
		
	}
	@Override
	public int insert(UserSetting userSetting) throws Exception {
		String key = Constants.user_setting_list_redis_key +":" + userSetting.getUserId();
		redisService.insertSet(key, userSetting);
		return userSettingMapper.insert(userSetting);
	}

	@Override
	public int update(UserSetting userSetting) throws Exception {
		List<String> keys = new ArrayList<String>();
		String key = Constants.user_setting_list_redis_key + ":" + userSetting.getUserId();
		if (redisService.exists(key)) {
			keys.add(key);
			redisService.delete(keys);
		}
		redisService.insertString(key, userSetting);
		return userSettingMapper.update(userSetting);
	}

	@Override
	public int deleteUserSettingByUserId(Integer userId) throws Exception {
		List<String> keys = new ArrayList<String>();
		String key = Constants.user_setting_list_redis_key + ":" + userId;
		if (redisService.exists(key)) {
			keys.add(key);
			redisService.delete(keys);
		}
		return userSettingMapper.deleteUserSettingByUserId(userId);
	}

	@Override
	public UserSetting findUserSettingByUserId(Integer userId) throws Exception {
		String key = Constants.user_setting_list_redis_key + ":" + userId;
		if (redisService.exists(key)) {
			Object object = redisService.select(key);
			if (object instanceof Set) {
				ArrayList<UserSetting> list = new ArrayList<UserSetting>((Set)object);
				return list.isEmpty() ? null : list.get(0);
			} else if (object instanceof UserSetting) {
				UserSetting us = (UserSetting) object;
				return us;
			}
		}
		return userSettingMapper.findUserSettingByUserId(userId);
	}
	@Override
	public List<UserSetting> findAllUserSetting() throws Exception {
		return userSettingMapper.findAllUserSetting();
	}
	
}
