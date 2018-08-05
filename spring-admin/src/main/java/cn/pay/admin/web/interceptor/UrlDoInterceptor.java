package cn.pay.admin.web.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.util.HttpSessionContext;

/**
 * 登陆拦截器
 * 
 * @author Administrator
 *
 */
public class UrlDoInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String servletPath = request.getServletPath();
		if (servletPath.endsWith(".do") || "/error".equals(servletPath)) {
			// 是否登录拦截
			LoginInfo loginInfo = HttpSessionContext.getCurrentLoginInfo();
			if (loginInfo == null) {
				response.sendRedirect("/login.html");
				return false;
			}
			return true;
		} else {
			// 不是.do结尾的请求返回404
			request.setAttribute("javax.servlet.error.status_code", 404);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/error");
			dispatcher.forward(request, response);
			return false;
		}
	}
}
