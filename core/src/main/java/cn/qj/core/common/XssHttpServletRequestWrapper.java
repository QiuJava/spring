package cn.qj.core.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 请求包装清除Xss和sql注入
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	@Override
	public String[] getParameterValues(String parameterName) {
		String[] values = super.getParameterValues(parameterName);
		if (values == null) {
			return null;
		}
		int len = values.length;
		String[] newValues = new String[len];
		for (int i = 0; i < len; i++) {
			newValues[i] = cleanXSS(values[i]);
		}
		return newValues;
	}

	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		return cleanXSS(value);
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null) {
			return null;
		}
		return cleanXSS(value);
	}

	private String cleanXSS(String str) {

		// 清除Html5和JavaScript
		str = str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		str = str.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		str = str.replaceAll("'", "&#39;");
		str = str.replaceAll("eval\\((.*)\\)", "");
		str = str.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		str = str.replaceAll("script", "");

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

		str = str.trim();
		return str;
	}
	
}