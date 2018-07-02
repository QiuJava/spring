package cn.pay.core.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import cn.pay.core.domain.sys.LoginInfo;

/**
 * 权限校验管理
 * 
 * @author Qiujian
 *
 */
@Component
public class AdminAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		// 没有配置权限控制直接过
		if (null == configAttributes || configAttributes.size() <= 0) {
			return;
		}
		// 超级管理员拥有所有权限
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			LoginInfo info = (LoginInfo) authentication.getPrincipal();
			if (info.isAdmin()) {
				return;
			}
		}

		ConfigAttribute config;
		String needRole;
		for (Iterator<ConfigAttribute> iter = configAttributes.iterator(); iter.hasNext();) {
			config = iter.next();
			needRole = config.getAttribute();
			for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
				if (needRole.equals(grantedAuthority.getAuthority())) {
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
