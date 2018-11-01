package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.EmailVerify;

/**
 * 邮箱校验信息持久化相关
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
public interface EmailVerifyRepository extends JpaRepository<EmailVerify, Long> {

	/**
	 * 根据验证码查询邮箱验证信息
	 * 
	 * @param verify
	 * @return
	 */
	EmailVerify findByVerify(String verify);

}
