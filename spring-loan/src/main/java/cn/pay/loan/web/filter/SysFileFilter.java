package cn.pay.loan.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 同步文件过滤
 * 
 * @author Qiujian
 *
 */
public class SysFileFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		String requestURI = req.getRequestURI();
		System.out.println("requestURI=" + requestURI);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
