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

	public static final String USERNAME_STR = "username";
	public static final String INIT_PASSWORD = "123456";
	public static final Md5PasswordEncoder MD5 = new Md5PasswordEncoder();

	public static final String URL_LOGIN_HTML = "/login.html";
	public static final String URL_LOGIN_INFO_LOGIN = "/loginInfo/login";
	public static final String URL_LOGIN_INFO_LOGOUT = "/loginInfo/logout";
	public static final String URL_LOGIN_INFO_AJAX = "/loginInfo/ajax";

}
