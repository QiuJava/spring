package cn.qj.config.listener;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureExpiredEvent;
import org.springframework.stereotype.Component;

import cn.qj.entity.LoginLog;
import cn.qj.service.LoginLogService;

/**
 * 账户或密码过期监听
 * 
 * @author Qiujian
 * @date 2019年4月12日
 *
 */
@Component
public class AuthenticationFailureExpiredListener implements ApplicationListener<AuthenticationFailureExpiredEvent> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginLogService loginLogService;

	@Override
	public void onApplicationEvent(AuthenticationFailureExpiredEvent event) {
		LoginLog log = new LoginLog();
		log.setIp(request.getRemoteAddr());
		log.setMsg(event.getException().getMessage());
		log.setStatus(LoginLog.ACCOUNT_EXPIRED);
		log.setUsername(event.getAuthentication().getName());
		log.setCreateTime(new Date());
		loginLogService.save(log);
	}

}
