package cn.qj.core.consts;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * 系统常量
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public class SysConst {
	private SysConst() {
	}

	public static final String ADMIN_NAME = "超级管理员";
	public static final String USERNAME_PARAM = "username";
	public static final String INIT_PASSWORD = "123456";

	public static final Md5PasswordEncoder MD5 = new Md5PasswordEncoder();

	public static final String LOGIN_ERR_MSG = "loginErrMsg";
	
	/** 邮箱验证码有效时间 */
	public static final int VALIDITY_DAY = 5;
	public static final Integer LOSER_MAX_COUNT = 5;

}
