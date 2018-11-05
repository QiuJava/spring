package cn.qj.core.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import cn.qj.core.entity.LoginInfo;

/**
 * 登录信息服务
 * 
 * @author Qiujian
 * @date 2018/11/01
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
	 * 是否存在超级管理员
	 * 
	 * @param isAdmin
	 * @return
	 */
	Boolean isExistAdmin(boolean isAdmin);

	/**
	 * 根据用户名判断用户名是否已存在
	 * 
	 * @param username
	 * @return
	 */
	Boolean isNotExistByUsername(String username);

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
