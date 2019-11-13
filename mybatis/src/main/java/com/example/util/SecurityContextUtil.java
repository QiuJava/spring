package com.example.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.vo.EmployeeVo;

/**
 * 安全上下文工具
 * 
 * @author Qiu Jian
 *
 */
public class SecurityContextUtil {
	private SecurityContextUtil() {
	}

	public static EmployeeVo getCurrentEmployeeVo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return (EmployeeVo) authentication.getPrincipal();
		}
		return null;
	}

	public static void logout() {
		SecurityContextHolder.clearContext();
	}
}
