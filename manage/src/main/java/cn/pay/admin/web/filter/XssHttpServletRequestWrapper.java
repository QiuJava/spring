package cn.pay.admin.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Xss过滤请求包装类
 * 
 * @author Administrator
 *
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
		str = str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		str = str.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		str = str.replaceAll("'", "&#39;");
		str = str.replaceAll("eval\\((.*)\\)", "");
		str = str.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		str = str.replaceAll("script", "");
		return str;
	}
}