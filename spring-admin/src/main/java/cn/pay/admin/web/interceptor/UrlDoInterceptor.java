package cn.pay.admin.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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
		if (servletPath.endsWith(".do")) {
			return true;
		} else {
			response.sendRedirect("/404.html");
			return false;
		}
		/*LoginInfo current = HttpSessionContext.getLoginInfoBySecurity();
		if (current != null && current.isAdmin()) {
			return true;
		}
		if (handler instanceof HandlerMethod) {
			// 真实类型
			HandlerMethod method = (HandlerMethod) handler;
			if (method.getMethodAnnotation(NoRequiredLogin.class) == null && current == null) {
				// 需要登陆，但是当前没有登陆
				response.sendRedirect("/login.html");
				return false;
			}
		}
		return true;*/
	}
}
