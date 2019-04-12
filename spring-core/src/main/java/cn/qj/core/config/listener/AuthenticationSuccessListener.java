package cn.qj.core.config.listener;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import cn.qj.core.entity.DataDict;
import cn.qj.core.entity.LoginLog;
import cn.qj.core.service.LoginLogService;
import cn.qj.core.util.DictUtil;

/**
 * 认证成功监听
 * 
 * @author Qiujian
 * @date 2019年4月12日
 *
 */
@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private HashOperations<String, String, Object> hashOperations;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		LoginLog log = new LoginLog();
		log.setLoginIp(request.getRemoteAddr());
		log.setLoginStatus(LoginLog.SUCCESS);
		log.setUsername(event.getAuthentication().getName());
		DataDict dict = (DataDict) hashOperations.get(ContextStartListener.DATA_DICT, DictUtil.LOGIN_SUCCESS_MSG);
		log.setLoginMsg(dict.getDictValue());
		loginLogService.save(log);
	}

}
