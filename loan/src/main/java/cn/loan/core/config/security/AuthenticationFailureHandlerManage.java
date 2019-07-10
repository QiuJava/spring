package cn.loan.core.config.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import cn.loan.core.controller.LoginUserController;
import cn.loan.core.service.LoginUserService;
import cn.loan.core.util.StringUtil;

/**
 * 管理认证失败处理
 * 
 * @author qiujian
 *
 */
@Configuration
public class AuthenticationFailureHandlerManage implements AuthenticationFailureHandler {

	@Autowired
	private LoginUserService loginUserService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		loginUserService.loginFailure(request);
		String msg = StringUtil.EMPTY;
		if (exception instanceof BadCredentialsException) {
			msg = "用户名或密码错误";
		} else if (exception instanceof InternalAuthenticationServiceException) {
			msg = exception.getMessage();
		}
		request.setAttribute(StringUtil.MSG, msg);
		RequestDispatcher dispatcher = request.getRequestDispatcher(LoginUserController.MANAGE_LOGINUSER_LOGINRESULT_MAPPING);
		dispatcher.forward(request, response);
	}

}
