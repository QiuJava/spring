package cn.qj.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 * 
 * @author Qiujian
 * @date 2018/12/17
 */
public class StringUtil {
	private StringUtil() {
	}

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
	
	public static boolean isPattern(String str, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean isNotEmpty(String str) {
		return str != null && str.length() > 0;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.equals("");
	}

	public static boolean lenValidator(String str, int start, int end) {
		return str.length() >= start && str.length() <= end;
	}

	public static String cleanXSS(String str) {
		// 清除Html5和JavaScript
		str = str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		str = str.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		str = str.replaceAll("'", "&#39;");
		str = str.replaceAll("eval\\((.*)\\)", "");
		str = str.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		str = str.replaceAll("script", "");
		return str;
	}

	public static String cleanSQL(String str) {
		// 清除SQL
		String[] strs = str.split(" ");
		String badStr = "drop|select|declare|information_schema.columns|use|insert|"
				+ "update|mid|delete|like'|truncate|and|by|sitename|create|from|where|"
				+ "xp_cmdshell|table|order|--|//|or|#|%|like|'|count|column_name|+|union|"
				+ "chr|net user|,|execute|-|master|/|group_concat|char|table_schema|;|grant|exec";
		String[] badStrs = badStr.split("\\|");
		for (int i = 0; i < badStrs.length; i++) {
			for (int j = 0; j < strs.length; j++) {
				if (strs[j].equalsIgnoreCase(badStrs[i])) {
					strs[j] = "";
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		str = sb.toString();

		return str;
	}

}
