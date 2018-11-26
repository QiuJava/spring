package cn.qj.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.qj.core.entity.LoginInfo;
import cn.qj.core.pojo.vo.VerifyCode;

/**
 * Servlet上下文
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public class HttpServletContext {
	private HttpServletContext() {
	}

	/** 当前登录信息 */
	public static final String CURRENT_LOGIN_INFO = "loginInfo";
	/** 手机验证码相关信息 */
	public static final String VERIFY_CODE = "VERIFY_CODE";
	/** Spring Security 放到session中的认证相关信息 */
	public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
	/** 绑定当前线程 */
	private static final ThreadLocal<HttpSession> THREADLOCAL = new ThreadLocal<>();

	public static UsernamePasswordAuthenticationToken getAuthenticationToken() {
		SecurityContextImpl impl = (SecurityContextImpl) getHttpSession().getAttribute(SPRING_SECURITY_CONTEXT);
		return (UsernamePasswordAuthenticationToken) impl.getAuthentication();
	}

	public static LoginInfo getLoginInfoBySecurity() {
		return (LoginInfo) getAuthenticationToken().getPrincipal();
	}

	public static LoginInfo getCurrentLoginInfo() {
		return (LoginInfo) getHttpSession().getAttribute(CURRENT_LOGIN_INFO);
	}

	public static void setCurrentLoginInfo(LoginInfo loginInfo) {
		getHttpSession().setAttribute(CURRENT_LOGIN_INFO, loginInfo);
	}

	public static VerifyCode getVerifyCode() {
		return (VerifyCode) getHttpSession().getAttribute(VERIFY_CODE);
	}

	public static void setVerifyCode(VerifyCode verifyCode) {
		getHttpSession().setAttribute(VERIFY_CODE, verifyCode);
	}

	public static HttpSession getHttpSession() {
		return getHttpServletRequest().getSession();
	}

	public static HttpServletRequest getHttpServletRequest() {
		ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return ra.getRequest();
	}

	public static void setSessionToThread(HttpSession session) {
		THREADLOCAL.set(session);
	}

	public static HttpSession getSessionByThread() {
		return THREADLOCAL.get();
	}

	public static void removeThread() {
		THREADLOCAL.remove();
	}
}
