package cn.qj.config.listener;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import cn.qj.config.properties.ConstProperties;
import cn.qj.config.properties.DictProperties;
import cn.qj.entity.Dict;
import cn.qj.entity.LoginLog;
import cn.qj.service.LoginLogService;

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

	@Autowired
	private ConstProperties constProperties;

	@Autowired
	private DictProperties dictProperties;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		LoginLog log = new LoginLog();
		log.setIp(request.getRemoteAddr());
		log.setStatus(LoginLog.SUCCESS);
		log.setUsername(event.getAuthentication().getName());
		Dict dict = (Dict) hashOperations.get(constProperties.getDictHash(), dictProperties.getLoginSuccessMsg());
		log.setMsg(dict.getValue());
		log.setCreateTime(new Date());
		loginLogService.save(log);
	}

}
