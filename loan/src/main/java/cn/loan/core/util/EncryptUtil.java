package cn.loan.core.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * 加密工具
 * 
 * @author qiujian
 *
 */
public class EncryptUtil {
	private EncryptUtil() {
	}

	public static final Md5PasswordEncoder MD5 = new Md5PasswordEncoder();

	public static String md5Encrypt(String salt, String... texts) {
		StringBuilder builder = new StringBuilder();
		if (texts != null) {
			for (String string : texts) {
				builder.append(string);
			}
		}
		return MD5.encodePassword(builder.toString(), salt);
	}
}
