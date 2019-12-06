package com.example.config.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.example.service.PermissionServiceImpl;
import com.example.util.StrUtil;


/**
 * 安全数据源过滤装置
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
	
	@Autowired
	private PermissionServiceImpl permissionService;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		FilterInvocation filterInvocation = (FilterInvocation) object;
		String requestUrl = filterInvocation.getRequest().getRequestURI();
		String authority = permissionService.getAuthorityByUrl(requestUrl);
		if(StrUtil.hasText(authority)) {
			return SecurityConfig.createList(authority);
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
