package cn.pay.loan.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import cn.pay.core.consts.SysConst;
import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.service.IpLogService;
import cn.pay.core.service.LoginInfoService;

/**
 * 自定义登录失败处理
 * 
 * @author Qiujian
 *
 */
@Component
public class LoanLoginFailureHandler implements AuthenticationFailureHandler {

	@Autowired
	private IpLogService ipLogService;
	@Autowired
	private LoginInfoService loginInfoService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (exception instanceof BadCredentialsException) {
			String username = request.getParameter(SysConst.USERNAME);
			LoginInfo loginInfo = loginInfoService.getByUsername(username);
			loginInfo.setLoserCount(loginInfo.getLoserCount() + 1);
			if (loginInfo.getLoserCount().equals(LoginInfo.LOSER_MAX_COUNT)) {
				// 达到次数进行锁定
				loginInfo.setStatus(LoginInfo.LOCK);
				loginInfo.setLockTime(new Date());
			}
			// 登录日志记录
			IpLog ipLog = new IpLog();
			ipLog.setIp(request.getRemoteAddr());
			ipLog.setUsername(username);
			ipLog.setUserType(LoginInfo.MANAGER);
			ipLog.setLoginTime(new Date());
			ipLog.setLoginState(IpLog.LOGIN_FAIL);
			loginInfoService.saveAndUpdate(loginInfo);
			ipLogService.saveAndUpdate(ipLog);
		}
		request.setAttribute("msg", exception.getMessage());
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(SysConst.URL_LOGIN_INFO_AJAX);
		requestDispatcher.forward(request, response);
	}

}
