package cn.qj.core.util;

/**
 * 字符串工具
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public class StringUtil {
	private StringUtil() {
	}

	public static boolean hasLength(String str) {
		return str != null && str.length() > 0;
	}

	public static boolean betweenLength(String str, int start, int end) {
		return str.length() >= start && str.length() <= end;
	}

}
