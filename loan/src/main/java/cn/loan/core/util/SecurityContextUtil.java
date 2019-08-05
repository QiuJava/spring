package cn.loan.core.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.loan.core.entity.LoginUser;

/**
 * 安全上下文工具
 * 
 * @author qiujian
 *
 */
public class SecurityContextUtil {
	private SecurityContextUtil() {
	}

	public static LoginUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		return (LoginUser) authentication.getPrincipal();
	}
}
