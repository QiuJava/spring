package cn.loan.core.config.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.controller.LoginUserController;
import cn.loan.core.entity.LoginLog;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.service.LoginLogService;
import cn.loan.core.service.LoginUserService;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 管理认证成功处理
 * 
 * @author qiujian
 *
 */
@Configuration
public class AuthenticationSuccessHandlerCustom implements AuthenticationSuccessHandler {

	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;
	@Autowired
	private LoginUserService loginUserService;
	@Autowired
	private LoginLogService loginLogService;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// 记录登录日志 清空的登录失败次数并修改用户状态为正常
		LoginLog loginLog = new LoginLog();
		loginLog.setUsername(authentication.getName());
		loginLog.setIpAddress(request.getRemoteAddr());
		SystemDictionary dict = systemDictionaryHashService.get(SystemDictionaryUtil.LOGIN_STATUS);
		String value = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.LOGIN_STATUS_SUCCESS, dict);
		Integer loginStatus = Integer.valueOf(value);
		loginLog.setLoginStatus(loginStatus);
		loginLogService.save(loginLog);

		LoginUser user = (LoginUser) authentication.getPrincipal();
		Integer loginFailureCount = user.getLoginFailureCount();
		// 清空失败次数
		if (loginFailureCount != null && loginFailureCount > 0) {
			user.setLoginFailureCount(null);
			user.setLockingTime(null);
			SystemDictionary userStatus = systemDictionaryHashService.get(SystemDictionaryUtil.USER_STATUS);
			String normal = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_STATUS_NORMAL, userStatus);
			Integer nomalStatus = Integer.valueOf(normal);
			user.setUserStatus(nomalStatus);
			loginUserService.save(user);
		}

		request.setAttribute(StringUtil.MSG, StringUtil.EMPTY);
		Integer manageType = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_TYPE,
				SystemDictionaryUtil.USER_TYPE_MANAGE, systemDictionaryHashService);
		Integer websiteType = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_TYPE,
				SystemDictionaryUtil.USER_TYPE_WEBSITE, systemDictionaryHashService);
		String forwardUrl = null;
		if (user.getUserType().equals(manageType)) {
			forwardUrl = LoginUserController.MANAGE_LOGINUSER_LOGINRESULT_MAPPING;
		} else if (user.getUserType().equals(websiteType)) {
			forwardUrl = LoginUserController.WEBSITE_LOGINUSER_LOGINRESULT_MAPPING;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl);
		dispatcher.forward(request, response);
	}

}
