package com.example.util;

/**
 * 字符串工具
 *
 * @author Qiu Jian
 *
 */
public class StrUtil {
	private StrUtil() {

	}

	/**
	 * 空字符串
	 */
	public static final String EMPTY_TEXT = "";

	/**
	 * 判断字符串对象有文本| "" =false, " " = false, null = false, " 1 " = true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasText(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * 判断字符串对象没有文本
	 * 
	 * @param str
	 * @return
	 */
	public static boolean noText(String str) {
		return str == null || str.trim().length() < 1;
	}

}
