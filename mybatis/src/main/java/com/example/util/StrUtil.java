package com.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * 手机号码正则
	 */
	public static final String PHONE_PATTERN = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
	/**
	 * 数字正则
	 */
	public static final String NUM_PATTERN = "[0-9]*";
	/**
	 * 中文正则
	 */
	public static final String ZW_PATTERN = "[\\u4e00-\\u9fa5]*";
	/**
	 * 字母正则
	 */
	public static final String ZM_PATTERN = "^[A-Za-z]+$";

	/**
	 * 是否匹配
	 * 
	 * @param str     字符串
	 * @param pattern 正则表达式
	 * @return
	 */
	public static boolean isPattern(String str, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

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
