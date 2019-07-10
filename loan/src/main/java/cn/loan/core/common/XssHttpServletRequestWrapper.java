package cn.loan.core.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.HtmlUtils;

import cn.loan.core.util.StringUtil;

/**
 * 请求包装清除Xss注入
 * 
 * @author Qiujian
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
			newValues[i] = HtmlUtils.htmlEscape(values[i], StringUtil.UTF8).trim();
		}
		return newValues;
	}

}