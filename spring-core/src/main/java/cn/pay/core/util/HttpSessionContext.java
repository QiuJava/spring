package cn.pay.core.util;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.obj.vo.VerifyCode;

/**
 * HttpSession 工具类
 * 
 * @author Qiujian
 *
 */
public class HttpSessionContext {
	private HttpSessionContext() {
	}

	/** 当前登录信息 */
	public static final String CURRENT_LOGIN_INFO = "CURRENT_LOGIN_INFO";
	/** 手机验证码相关信息 */
	public static final String VERIFY_CODE = "VERIFY_CODE";
	/** Spring Security 放到session中的认证相关信息 */
	public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
	/** 绑定当前线程 */
	private static ThreadLocal<HttpSession> threadLocal = new ThreadLocal<>();
	
	public static UsernamePasswordAuthenticationToken getAuthenticationToken() {
		SecurityContextImpl impl = (SecurityContextImpl) HttpSessionContext.getHttpSession()
				.getAttribute(SPRING_SECURITY_CONTEXT);
		return (UsernamePasswordAuthenticationToken) impl.getAuthentication();
	}
	
	public static LoginInfo getLoginInfoBySecurity() {
		return (LoginInfo)HttpSessionContext.getAuthenticationToken().getPrincipal();
	}

	public static LoginInfo getCurrentLoginInfo() {
		return (LoginInfo) HttpSessionContext.getHttpSession().getAttribute(CURRENT_LOGIN_INFO);
	}

	public static void setCurrentLoginInfo(LoginInfo loginInfo) {
		HttpSessionContext.getHttpSession().setAttribute(CURRENT_LOGIN_INFO, loginInfo);
	}

	public static VerifyCode getVerifyCode() {
		return (VerifyCode) HttpSessionContext.getHttpSession().getAttribute(VERIFY_CODE);
	}

	public static void setVerifyCode(VerifyCode verifyCode) {
		HttpSessionContext.getHttpSession().setAttribute(VERIFY_CODE, verifyCode);
	}

	public static HttpSession getHttpSession() {
		ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return ra.getRequest().getSession();
	}

	public static void setSessionToThread(HttpSession session) {
		threadLocal.set(session);
	}

	public static HttpSession getSessionByThread() {
		return threadLocal.get();
	}

	public static void removeThread() {
		threadLocal.remove();
	}
}
