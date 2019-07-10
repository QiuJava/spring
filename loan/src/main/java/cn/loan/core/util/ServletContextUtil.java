package cn.loan.core.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Servlet工具
 * 
 * @author Qiujian
 * 
 */
public class ServletContextUtil {
	private ServletContextUtil() {
	}

	public static HttpServletRequest getCurrentHttpRequest() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attr.getRequest();
	}

	public static String getCurrentHttpSessionId() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attr.getRequest().getSession().getId();
	}

}
