package cn.pay.admin.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import cn.pay.core.consts.SysConst;
import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.service.IpLogService;

/**
 * 自定义登录成功处理
 * 记录登录日志
 * 
 * @author Qiujian
 *
 */
@Component
public class AdminLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private IpLogService ipLogService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		LoginInfo loginInfo = (LoginInfo)authentication.getPrincipal();
		// 登录日志记录
		IpLog ipLog = new IpLog();
		ipLog.setIp(request.getRemoteAddr());
		ipLog.setUsername(loginInfo.getUsername());
		ipLog.setUserType(LoginInfo.MANAGER);
		ipLog.setLoginTime(new Date());
		ipLog.setLoginState(IpLog.SUCCESS);
		ipLogService.saveAndUpdate(ipLog);
		
		response.sendRedirect(SysConst.INDEX_DO);
	}

}
