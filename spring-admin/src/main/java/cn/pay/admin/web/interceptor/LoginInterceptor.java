package cn.pay.admin.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.pay.core.consts.SysConst;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.obj.annotation.NoRequiredLogin;
import cn.pay.core.util.HttpSessionContext;

/**
 * 登陆拦截
 * 
 * @author Administrator
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LoginInfo current = HttpSessionContext.getCurrentLoginInfo();
		if (current != null && SysConst.ADMIN.equals(current.getUsername())) {
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
		return true;
	}
}
