package cn.qj.config.listener;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import cn.qj.entity.LoginLog;
import cn.qj.service.LoginLogService;

/**
 * 密码错误监听
 * 
 * @author Qiujian
 * @date 2019年4月12日
 *
 */
@Component
public class AuthenticationFailureBadCredentialsListener
		implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginLogService loginLogService;

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

		if (event.getException() instanceof UsernameNotFoundException) {
			return;
		}
		LoginLog log = new LoginLog();
		log.setLoginIp(request.getRemoteAddr());
		log.setLoginStatus(LoginLog.BAD_CREDENTIALS);
		log.setUsername(event.getAuthentication().getName());
		log.setLoginMsg(event.getException().getMessage());
		loginLogService.save(log);
	}

}
