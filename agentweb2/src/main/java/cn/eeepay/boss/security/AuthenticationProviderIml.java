package cn.eeepay.boss.security;

import cn.eeepay.framework.dao.SysConfigDao;
import cn.eeepay.framework.dao.UserInfoDao;
import cn.eeepay.framework.encryptor.rsa.RSAUtils;
import cn.eeepay.framework.model.AgentOperLogBean;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentOperLogService;
import cn.eeepay.framework.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

/**
 * 
 * @author Administrator
 *
 */
public class AuthenticationProviderIml implements AuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationProviderIml.class);

	@Autowired
	private UserDetailsService userDetailsService;
	@Resource
	private SysConfigDao sysConfigDao;
	@Resource
	private UserInfoDao userInfoDao;
	@Resource
	private HttpServletRequest request;
	@Autowired
	private AgentOperLogService agentOperLogService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		// 页面输入的密码
		String password = (String) authentication.getCredentials();
		// 私钥解密password
		try {
			password = RSAUtils.decryptDataOnJava(password, Constants.PRIVATE_KEY);
		} catch (Exception e) {
			logger.error("RSA解密错误", e);
			throw new BadCredentialsException("密码错误");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		if (userDetails == null){
			logger.warn("账号或密码不正确");
			throw new BadCredentialsException("账号或密码不正确");
		}
		UserLoginInfo userLoginInfo = (UserLoginInfo) userDetails;
		if (userLoginInfo.getLockTime() != null) {
			int lockTime = queryLoginLockTime();
			int diffTime = (int) ((new Date().getTime() - userLoginInfo.getLockTime().getTime()) / (1000 * 60));
			if (diffTime >= 0 && diffTime < lockTime){
				int remainLockTime = lockTime - diffTime;
				logger.warn("您的账号被锁定");
				request.setAttribute("error", "lock");
				request.setAttribute("lockTime", remainLockTime <= 0 ? "1" : remainLockTime + "");
				throw new BadCredentialsException("您的账号被锁定");
			}
		}

		// 密码匹配
		Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		String encodePassword = md5PasswordEncoder.encodePassword(password, userDetails.getUsername());
		if (encodePassword.equals(userDetails.getPassword())) {
			logger.warn("清空登陆错误次数");
			logger.info("用户id {}, 代理商编号 {}, 代理商名称:{} 进行登陆操作, 登陆 ip: {}", userLoginInfo.getUserInfoId(),
					userLoginInfo.getUserEntityInfo().getEntityId(), userLoginInfo.getRealName(),
					getRemoteAddr(request));
//			AgentOperLogBean loginBean = new AgentOperLogBean();
//			loginBean.setAgentName(userLoginInfo.getRealName());
//			loginBean.setAgentNo(userLoginInfo.getUserEntityInfo().getEntityId());
//			loginBean.setMethodDesc("登录操作");
//			loginBean.setOperIp(getRemoteAddr(request));
//			agentOperLogService.insertLog(loginBean);
			userInfoDao.clearWrongPasswordCount(userLoginInfo.getUserInfoId());
		}else{
			if (userLoginInfo.getWrongPasswordCount() + 1 < queryWrongPasswordMaxCount()) {
				userInfoDao.increaseWrongPasswordCount(userLoginInfo.getUserInfoId());
			}else{
				userInfoDao.lockLoginUser(userLoginInfo.getUserInfoId());
			}
			logger.warn("密码错误");
			throw new BadCredentialsException("密码错误");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	private String getRemoteAddr(HttpServletRequest request){
		try {
			String xforwardedFor = request.getHeader("x-forwarded-for");
			String xRealIp = request.getHeader("X-Real-IP");
			logger.info("请求头数据: key = x-forwarded-for, value = {}", xforwardedFor);
			logger.info("请求头数据: key = X-Real-IP, value = {}", xRealIp);
			if (StringUtils.isNotBlank(xforwardedFor)) {
				return xforwardedFor.split(",")[0];
			}
			if (StringUtils.isNotBlank(xRealIp)) {
				return xRealIp;
			}
			return request.getRemoteAddr();
		}catch (Exception e){
			logger.error("getRemoteAddr ==> " + e);
			return "";
		}
	}

	public int queryWrongPasswordMaxCount(){
		String maxCount = sysConfigDao.getStringValueByKey("agent_web_login_wrong_password_max_count");
		try {
			return Integer.valueOf(maxCount);
		}catch (Exception e){
			return 5;
		}
	}

	public int queryLoginLockTime(){
		String maxCount = sysConfigDao.getStringValueByKey("agent_web_login_lock_time");
		try {
			return Integer.valueOf(maxCount);
		}catch (Exception e){
			return 30;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
