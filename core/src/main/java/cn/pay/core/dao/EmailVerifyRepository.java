package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.business.EmailVerify;

/**
 * 邮箱校验信息持久化相关
 * 
 * @author Qiujian
 * @date 2018年8月10日
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
