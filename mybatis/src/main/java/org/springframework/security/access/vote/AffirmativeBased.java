package org.springframework.security.access.vote;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import com.example.entity.Employee;
import com.example.vo.EmployeeVo;
import com.example.vo.PermissionVo;

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

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException {
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			FilterInvocation filterInvocation = (FilterInvocation) object;
			String requestUrl = filterInvocation.getRequest().getRequestURI();

			EmployeeVo employeeVo = (EmployeeVo) authentication.getPrincipal();
			// 超级管理员拥有所有权限 所有用户拥有首页权限
			if (employeeVo.getSuperAdmin() == Employee.IS_ADMIN || "/".equals(requestUrl)) {
				return;
			}
			@SuppressWarnings("unchecked")
			List<PermissionVo> pemissionVoList = (List<PermissionVo>) employeeVo.getAuthorities();
			for (PermissionVo permissionVo : pemissionVoList) {
				// 拥有访问权限
				if (requestUrl.equals(permissionVo.getUrl())) {
					return;
				}
			}
		}
		throw new AccessDeniedException("没有权限");
	}
}
