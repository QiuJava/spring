package cn.qj.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import cn.qj.config.listener.ContextStartListener;
import cn.qj.entity.Permission;

/**
 * 初始化权限
 * 
 * @author Qiujian
 * @date 2018/8/13
 */
@Component
@SuppressWarnings("unchecked")
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		List<Permission> permissions = (List<Permission>) valueOperations.get(ContextStartListener.PERMISSION);
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		for (Permission permission : permissions) {
			AntPathRequestMatcher matcher = new AntPathRequestMatcher(permission.getUrl());
			if (matcher.matches(request)) {
				List<ConfigAttribute> configList = new ArrayList<>();
				configList.add(new SecurityConfig(permission.getAuthority()));
				return configList;
			}
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		List<ConfigAttribute> configList = new ArrayList<>();
		return configList;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
