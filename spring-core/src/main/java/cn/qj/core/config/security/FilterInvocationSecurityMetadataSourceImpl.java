package cn.qj.core.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import cn.qj.core.entity.Authority;
import cn.qj.core.service.AuthorityService;

/**
 * 初始化权限
 * 
 * @author Qiujian
 * @date 2018/8/13
 */
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private AuthorityService authorityService;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		List<Authority> authorities = authorityService.getAll();
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		for (Authority anth : authorities) {
			AntPathRequestMatcher matcher = new AntPathRequestMatcher(anth.getAuthority().replace("ROLE_", ""));
			if (matcher.matches(request)) {
				List<ConfigAttribute> configList = new ArrayList<>();
				configList.add(new SecurityConfig(anth.getAuthority()));
				return configList;
			}
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		List<Authority> authorities = authorityService.getAll();
		List<ConfigAttribute> configList = new ArrayList<>();
		for (Authority anth : authorities) {
			configList.add(new SecurityConfig(anth.getAuthority()));
		}
		return configList;

	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
