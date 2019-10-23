package cn.eeepay.boss.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import cn.eeepay.framework.model.bill.BlockedIp;
import cn.eeepay.framework.service.bill.BlockedIpService;
import cn.eeepay.framework.util.DateUtil;

@SuppressWarnings("rawtypes")
@Component
public class AuthenticationLoginListener 
  implements ApplicationListener  {
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationLoginListener.class);
	
	@Resource
	private BlockedIpService blockedIpService;
	
//	@Value("${login.denyip.times:5}")
//	private int loginErrorTimes;//登录最大错误次数
//	
//	@Value("${login.denyip.minutes:30}")
//	private int loginErrorMinutes;
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof AuthenticationFailureBadCredentialsEvent) {
			 AuthenticationFailureBadCredentialsEvent authEvent = (AuthenticationFailureBadCredentialsEvent) event;
			 log.info("AuthenticationFailureBadCredentialsEvent");
			WebAuthenticationDetails auth = (WebAuthenticationDetails) 
					authEvent.getAuthentication().getDetails();
	        try {
	            String denyIp = auth.getRemoteAddress();
	            String denyDay = DateUtil.getCurrentDate();
	            BlockedIp blockedIp = new BlockedIp();
	            blockedIp.setDenyIp(denyIp);
            	blockedIp.setDenyDay(denyDay);
	            blockedIpService.BlockedIp(blockedIp);
			} catch (Exception e) {
				log.error("异常:",e);
			}
		}
		if (event instanceof AuthenticationSuccessEvent) {
			AuthenticationSuccessEvent authEvent = (AuthenticationSuccessEvent) event;
			log.info("AuthenticationSuccessEvent");
			WebAuthenticationDetails auth = (WebAuthenticationDetails) 
			 authEvent.getAuthentication().getDetails();
	         try {
	    	    String denyIp = auth.getRemoteAddress();
	    	    String denyDay = DateUtil.getCurrentDate();
	    	    BlockedIp blockedIp = blockedIpService.findBlockedIpByIpAndDate(denyDay,denyIp);
	    	    if (blockedIp != null) {
	    	    	blockedIpService.deleteBlockedIp(blockedIp);
				}
			} catch (Exception e) {
				log.error("异常:",e);
			}
		}
		
	}

 

 
}