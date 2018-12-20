package cn.qj.key.util;

/**
 * 字符串工具
 * 
 * @author Qiujian
 * @date 2018/12/17
 */
public class StrUtil {
	private StrUtil() {
	}

	public static boolean notEmpty(String str) {
		return str != null && str.length() > 0;
	}

	public static String cleanXSS(String str) {
		// 清除Html5和JavaScript
		str = str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		str = str.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		str = str.replaceAll("'", "&#39;");
		str = str.replaceAll("eval\\((.*)\\)", "");
		str = str.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		str = str.replaceAll("script", "");
		str = str.replaceAll("[*]", "[" + "*]");
		str = str.replaceAll("[+]", "[" + "+]");
		str = str.replaceAll("[?]", "[" + "?]");
		return str;
	}

	public static String cleanSQL(String str) {
		// 清除SQL
		String[] strs = str.split(" ");
		String badStr = "drop|select|declare|information_schema.columns|use|insert|"
				+ "update|mid|delete|like'|truncate|and|by|sitename|create|from|where|"
				+ "xp_cmdshell|table|order|--|//|or|#|%|like|'|count|column_name|+|union|"
				+ "chr|net user|,|execute|-|master|/|group_concat|char|table_schema|;|grant|exec\r\n";
		String[] badStrs = badStr.split("\\|");
		for (int i = 0; i < badStrs.length; i++) {
			for (int j = 0; j < strs.length; j++) {
				if (strs[j].equalsIgnoreCase(badStrs[i])) {
					strs[j] = "forbid";
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
