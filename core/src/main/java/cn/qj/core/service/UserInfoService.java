package cn.qj.core.service;

import cn.qj.core.entity.UserInfo;

/**
 * 用户信息服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public interface UserInfoService {

	/**
	 * 获取用户信息
	 * 
	 * @param id
	 * @return
	 */
	UserInfo get(Long id);

	/**
	 * 保存用户基本资料信息
	 * 
	 * @param userInfo
	 */
	void saveBasicInfo(UserInfo userInfo);

	/**
	 * 绑定手机号
	 * 
	 * @param phoneNumber
	 * @param verifyCode
	 */
	void bind(String phoneNumber, String verifyCode);

	/**
	 * 保存和更新 并判断乐观锁是否成功
	 * 
	 * @param userInfo
	 */
	void update(UserInfo userInfo);

	/**
	 * 保存
	 * 
	 * @param userInfo
	 */
	void save(UserInfo userInfo);
}
