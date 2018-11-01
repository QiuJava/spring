package cn.qj.admin.config.security;

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

import cn.qj.core.entity.LoginInfo;

/**
 * 权限校验管理
 * 
 * @author Qiujian
 * @date 2018/8/13
 */
@Component
public class AdminAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			LoginInfo info = (LoginInfo) authentication.getPrincipal();
			// 超级管理员拥有所有权限
			if (info.getIsAdmin()) {
				return;
			}
		} else {
			throw new InsufficientAuthenticationException("请先登录");
		}

		// 没有配置权限控制直接过
		if (null == configAttributes || configAttributes.size() <= 0) {
			return;
		}

		ConfigAttribute config = null;
		String needRole = null;
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
