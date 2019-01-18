package cn.qj.admin.config.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import cn.qj.core.consts.RequestConst;
import cn.qj.core.entity.IpLog;
import cn.qj.core.entity.LoginInfo;
import cn.qj.core.service.IpLogService;
import cn.qj.core.util.HttpServletContext;

/**
 * 登录成功处理
 * 
 * @author Qiujian
 * @date 2018/8/13
 */
@Component
public class AdminLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private IpLogService ipLogService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		LoginInfo loginInfo = (LoginInfo) authentication.getPrincipal();
		// 登录日志记录
		IpLog ipLog = new IpLog();
		Date currentDate = new Date();
		ipLog.setIp(request.getRemoteAddr());
		ipLog.setUsername(loginInfo.getUsername());
		ipLog.setUserType(loginInfo.getUserType());
		ipLog.setLoginTime(currentDate);
		ipLog.setLoginState(IpLog.LOGIN_SUCCESS);
		ipLog.setGmtCreate(currentDate);
		ipLog.setGmtModified(currentDate);
		ipLogService.saveIpLog(ipLog);
		// 登录成功把用户登录信息存储到session
		HttpServletContext.setCurrentLoginInfo(loginInfo);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(RequestConst.LOGIN_INFO_AJAX);
		requestDispatcher.forward(request, response);
	}

}
