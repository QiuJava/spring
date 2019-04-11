package cn.qj.core.util;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * HttpSession 工具
 * 
 * @author Qiujian
 * @date 2019年4月8日
 *
 */
public class HttpSessionUtil {

	public static HttpSession get() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpSession session = requestAttributes.getRequest().getSession();
		return session;
	}

	public String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
