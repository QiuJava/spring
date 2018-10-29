package cn.pay.core.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import cn.pay.core.entity.sys.LoginInfo;

/**
 * 用户登录
 * 
 * @author Qiu jian
 *
 */
public interface LoginInfoService extends UserDetailsService {

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
	Boolean isExistByUsername(String username);

	/**
	 * 获取用户id获取登录信息
	 * 
	 * @param id
	 * @return
	 */
	LoginInfo getLoginInfoById(Long id);

	/**
	 * 根据登录名获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	LoginInfo getLoginInfoByUsername(String username);

	/**
	 * 查询所有用户
	 * 
	 * @return
	 */
	List<LoginInfo> listAll();

	/**
	 * 更新登录信息
	 * 
	 * @param loginInfo
	 * @return
	 */
	LoginInfo updateLoginInfo(LoginInfo loginInfo);

	/**
	 * 保存登录信息
	 * 
	 * @param loginInfo
	 * @return
	 */
	LoginInfo saveLoginInfo(LoginInfo loginInfo);

}
