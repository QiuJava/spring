package cn.pay.core.util;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.obj.vo.VerifyCode;

/**
 * Http会话工具类
 * 
 * @author Qiujian
 *
 */
public class HttpSessionContext {
	private HttpSessionContext() {
	}

	public static final String LOGIN_INFO_IN_SESSIION = "loginInfo";
	public static final String VERIFY_CODE = "verifyCode";

	private static ThreadLocal<HttpSession> threadLocal = new ThreadLocal<>();

	public static void setVerifyCode(VerifyCode verifyCode) {
		HttpSessionContext.getHttpSession().setAttribute(VERIFY_CODE, verifyCode);
	}

	public static VerifyCode getVerifyCode() {
		return (VerifyCode) HttpSessionContext.getHttpSession().getAttribute(VERIFY_CODE);
	}

	public static HttpSession getHttpSession() {
		ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return ra.getRequest().getSession();
	}

	public static void setCurrentLoginInfo(LoginInfo current) {
		HttpSessionContext.getHttpSession().setAttribute(LOGIN_INFO_IN_SESSIION, current);
	}

	public static LoginInfo getCurrentLoginInfo() {
		return (LoginInfo) HttpSessionContext.getHttpSession().getAttribute(LOGIN_INFO_IN_SESSIION);
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
