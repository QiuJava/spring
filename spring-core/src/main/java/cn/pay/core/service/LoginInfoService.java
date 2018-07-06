package cn.pay.core.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import cn.pay.core.domain.sys.LoginInfo;

/**
 * 用户登录
 * 
 * @author Administrator
 *
 */
public interface LoginInfoService extends UserDetailsService {

	/**
	 * 用户登录验证
	 * 
	 * @param username
	 * @param password
	 * @param ip
	 * @param manager
	 * @return
	 */
	//LoginInfo login(String username, String password, String ip, Integer manager);

	/**
	 * 前台页面注册
	 * 
	 * @param username
	 * @param password
	 */
	void register(String username, String password);

	/**
	 * 根据用户名判断用户名是否已存在
	 * 
	 * @param username
	 * @return
	 */
	boolean isExist(String username);

	/**
	 * 获取用户登录信息
	 * 
	 * @param id
	 * @return
	 */
	LoginInfo get(Long id);

	/**
	 * 根据登录名获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	LoginInfo getByUsername(String username);

	/**
	 * 保存或更新
	 * 
	 * @param info
	 */
	void saveAndUpdate(LoginInfo info);

}
