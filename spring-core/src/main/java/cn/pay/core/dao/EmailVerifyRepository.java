package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.business.EmailVerify;

public interface EmailVerifyRepository extends JpaRepository<EmailVerify, Long> {

	/**
	 * 根据验证码查询邮箱验证信息
	 * 
	 * @param verify
	 * @return
	 */
	EmailVerify findByVerify(String verify);

}
