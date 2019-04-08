package cn.qj.core.web.filter;

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
 * 请求过滤器
 * 
 * @author Qiujian
 * @date 2018/12/18
 */
@WebFilter(filterName = "requestFilter", urlPatterns = "/*")
public class RequestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		chain.doFilter(new HttpServletRequestWrapperImpl(req), response);
	}

	@Override
	public void destroy() {

	}

}
