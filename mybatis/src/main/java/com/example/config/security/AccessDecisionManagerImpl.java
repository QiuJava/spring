package com.example.config.security;

import java.util.Collection;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.example.entity.Employee;

/**
 * 自定义访问决策
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class AccessDecisionManagerImpl implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		Employee employee =  (Employee)authentication.getPrincipal();
		if (employee.getSuperAdmin() == Employee.IS_ADMIN) {
			return ;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			for (ConfigAttribute attr : configAttributes) {
				if (authority.getAuthority().equals(attr.getAttribute())) {
					return;
				}
			}
		}
		throw new AccessDeniedException("没有权限");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
