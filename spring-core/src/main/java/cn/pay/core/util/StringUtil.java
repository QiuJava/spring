package cn.pay.core.util;

/**
 * 系统字符串操作工具
 * 
 * @author Administrator
 *
 */
public class StringUtil {
	private StringUtil() {
	}

	public static boolean hasLength(String str) {
		return str != null && str.length() > 0;
	}

}
