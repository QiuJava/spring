package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.LoginUser;

/**
 * 登录用户数据操作
 * 
 * @author qiujian
 *
 */
public interface LoginUserDao extends JpaRepository<LoginUser, Long> {

	/**
	 * 根据用户名查找登录用户
	 * 
	 * @param username
	 * @return
	 */
	LoginUser findByUsername(String username);

	/**
	 * 根据用户类型统计用户
	 * 
	 * @param userType
	 * @return
	 */
	Long countByUserType(Integer userType);

	/**
	 * 根据用户名统计用户
	 * 
	 * @param username
	 * @return
	 */
	Long countByUsername(String username);

	/**
	 * 根据用户名和用户类型查找用户
	 * 
	 * @param username
	 * @param userType
	 * @return
	 */
	LoginUser findByUsernameAndUserType(String username, Integer userType);

}
