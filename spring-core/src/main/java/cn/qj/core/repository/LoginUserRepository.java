package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.LoginUser;

/**
 * 登录用户数据操作
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {

	/**
	 * 根据用户名查询登录用户
	 * 
	 * @param username
	 * @return
	 */
	LoginUser findByUsername(String username);

}
