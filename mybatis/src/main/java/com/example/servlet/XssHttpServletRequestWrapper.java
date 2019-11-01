package com.example.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.HtmlUtils;


/**
 * 请求包装清除Xss注入
 * 
 * @author Qiu Jian
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
			newValues[i] = HtmlUtils.htmlEscape(values[i], "utf-8").trim();
		}
		return newValues;
	}

}