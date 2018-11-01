package cn.qj.admin.config.security;

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

import cn.qj.core.consts.SysConst;
import cn.qj.core.entity.IpLog;
import cn.qj.core.entity.LoginInfo;
import cn.qj.core.service.IpLogService;
import cn.qj.core.service.LoginInfoService;

/**
 * 登录失败处理
 * 
 * @author Qiujian
 * @date 2018/8/13
 */
@Component
public class AdminLoginFailureHandler implements AuthenticationFailureHandler {

	public static final String LOGIN_ERR_MSG = "loginErrMsg";

	@Autowired
	private IpLogService ipLogService;
	@Autowired
	private LoginInfoService loginInfoService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (exception instanceof BadCredentialsException) {
			String username = request.getParameter(SysConst.USERNAME_STR);
			LoginInfo loginInfo = loginInfoService.getLoginInfoByUsername(username);
			loginInfo.setLoserCount(loginInfo.getLoserCount() + 1);

			Integer loserCount = loginInfo.getLoserCount();
			Date currentDate = new Date();
			// 达到次数进行锁定
			if (loserCount >= LoginInfo.LOSER_MAX_COUNT) {
				loginInfo.setStatus(LoginInfo.LOCK);
				loginInfo.setLockTime(currentDate);
				loginInfo.setGmtModified(currentDate);
			}
			// 登录日志记录
			IpLog ipLog = new IpLog();
			ipLog.setIp(request.getRemoteAddr());
			ipLog.setUsername(username);
			ipLog.setUserType(LoginInfo.MANAGER);
			ipLog.setLoginTime(currentDate);
			ipLog.setLoginState(IpLog.LOGIN_FAIL);
			ipLog.setGmtCreate(currentDate);
			ipLog.setGmtModified(currentDate);

			loginInfoService.saveLoginInfo(loginInfo);
			ipLogService.saveIpLog(ipLog);
		}
		request.setAttribute(LOGIN_ERR_MSG, exception.getMessage());
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(SysConst.URL_LOGIN_INFO_AJAX);
		requestDispatcher.forward(request, response);
	}

}
