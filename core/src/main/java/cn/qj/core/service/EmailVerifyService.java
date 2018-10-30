package cn.qj.core.service;

/**
 * 邮箱验证服务
 * 
 * @author Administrator
 *
 */
public interface EmailVerifyService {

	/**
	 * 根据邮箱验证码进行校验
	 * 
	 * @param verify
	 */
	void verifyEmail(String verify);

	/**
	 * 发送邮箱验证码相关信息到用户邮箱
	 * 
	 * @param email
	 */
	void send(String email);

}
