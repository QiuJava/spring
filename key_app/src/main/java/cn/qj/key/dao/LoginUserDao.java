package cn.qj.key.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.key.entity.LoginUser;

/**
 * 登录用户数据操作对象
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
public interface LoginUserDao extends JpaRepository<LoginUser, Long> {

	long countByUsername(String username);

	long countByUsernameNotNull();

	long countByPhoneNum(String phoneNum);

}
