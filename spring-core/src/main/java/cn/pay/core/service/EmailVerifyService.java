package cn.pay.core.service;

/**
 * 邮箱验证
 * 
 * @author Administrator
 *
 */
public interface EmailVerifyService {

	/**
	 * 邮箱验证
	 * 
	 * @param verify
	 */
	void verifyEmail(String verify);

	/**
	 * 发送认证信息到用户邮箱
	 * 
	 * @param email
	 */
	void send(String email);

}
