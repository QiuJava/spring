
package com.example.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Xss过滤
 * 
 * @author Qiu Jian
 * 
 */
@WebFilter(filterName = "XssFilter",urlPatterns = "/*")
public class XssFilter implements Filter {

	FilterConfig filterConfig = null;

	@Override
	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		chain.doFilter(new XssHttpServletRequestWrapper(req), response);
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

}
