package org.springframework.security.access.vote;

import java.util.*;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import com.example.entity.Employee;
import com.example.entity.Permission;

/**
 * 修改Securuty框架权限校验
 * 
 * @author Qiu Jian
 *
 */
public class AffirmativeBased extends AbstractAccessDecisionManager {

	public AffirmativeBased(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
		super(decisionVoters);
	}

	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException {
		if (authentication instanceof AnonymousAuthenticationToken) {
			throw new AccessDeniedException("请先登录");
		}
		Employee employee = (Employee) authentication.getPrincipal();
		FilterInvocation filterInvocation = (FilterInvocation) object;
		String requestUrl = filterInvocation.getRequest().getRequestURI();
		// 超级管理员拥有所有权限 所有用户拥有首页权限
		if (employee.getSuperAdmin() == Employee.IS_ADMIN || "/".equals(requestUrl)) {
			return;
		}
		@SuppressWarnings("unchecked")
		List<Permission> pemissionList = (List<Permission>) employee.getAuthorities();
		for (Permission permission : pemissionList) {
			// 拥有访问权限
			if (requestUrl.equals(permission.getUrl())) {
				return;
			}
		}
		StringBuilder builder = new StringBuilder(100);
		builder.append(employee.getUsername());
		builder.append("没有");
		builder.append(requestUrl);
		builder.append("权限");
		throw new AccessDeniedException(builder.toString());
	}
}
