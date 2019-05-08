package cn.qj.config.listener;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureCredentialsExpiredEvent;
import org.springframework.stereotype.Component;

import cn.qj.entity.LoginLog;
import cn.qj.service.LoginLogService;

/**
 * 密码过期监听
 * 
 * @author Qiujian
 * @date 2019年4月12日
 *
 */
@Component
public class AuthenticationFailureCredentialsExpiredListener
		implements ApplicationListener<AuthenticationFailureCredentialsExpiredEvent> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginLogService loginLogService;

	@Override
	public void onApplicationEvent(AuthenticationFailureCredentialsExpiredEvent event) {
		LoginLog log = new LoginLog();
		log.setIp(request.getRemoteAddr());
		log.setStatus(LoginLog.CREDENTIALS_EXPIRED);
		log.setUsername(event.getAuthentication().getName());
		log.setMsg(event.getException().getMessage());
		log.setCreateTime(new Date());
		loginLogService.save(log);
	}

}
