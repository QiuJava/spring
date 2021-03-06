package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.LoginInfo;

/**
 * 登录信息持久化相关
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

	/**
	 * 根据用户类型统计用户数
	 * 
	 * @param userType
	 * @return
	 */
	int countByUserType(int userType);

	/**
	 * 根据用户名精确查找用户
	 * 
	 * @param username
	 * @return
	 */
	LoginInfo findByUsername(String username);

	/**
	 * 前后台登录查询
	 * 
	 * @param username
	 * @param password
	 * @param userType
	 * @return
	 */
	LoginInfo findByUsernameAndPasswordAndUserType(String username, String password, Integer userType);

	/**
	 * 根据用户名查询登录信息是否存在
	 * 
	 * @param username
	 * @return
	 */
	int countByUsername(String username);

}
