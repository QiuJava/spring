package com.example.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.entity.Employee;

/**
 * 安全上下文工具
 * 
 * @author Qiu Jian
 *
 */
public class SecurityContextUtil {
	private SecurityContextUtil() {
	}

	public static Employee getCurrentEmployee() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return (Employee) authentication.getPrincipal();
		}
		return null;
	}

}
