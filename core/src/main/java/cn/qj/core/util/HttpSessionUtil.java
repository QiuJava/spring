package cn.qj.core.util;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.qj.core.entity.LoginInfo;
import cn.qj.core.pojo.vo.VerifyCode;

/**
 * HttpSession 工具
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public class HttpSessionUtil {
	private HttpSessionUtil() {
	}

	/** 手机验证码相关信息 */
	public static final String VERIFY_CODE = "VERIFY_CODE";
	/** 绑定当前线程 */
	private static final ThreadLocal<HttpSession> THREADLOCAL = new ThreadLocal<>();

	public static LoginInfo getCurrentLoginInfo() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		return authentication == null ? null : (LoginInfo) authentication.getPrincipal();
	}

	public static VerifyCode getVerifyCode() {
		return (VerifyCode) getHttpSession().getAttribute(VERIFY_CODE);
	}

	public static void setVerifyCode(VerifyCode verifyCode) {
		getHttpSession().setAttribute(VERIFY_CODE, verifyCode);
	}

	public static HttpSession getHttpSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attr.getRequest().getSession();
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
