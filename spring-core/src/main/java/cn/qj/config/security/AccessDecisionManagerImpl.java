package cn.qj.config.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import cn.qj.config.properties.ConstProperties;
import cn.qj.config.properties.DictProperties;
import cn.qj.entity.Dict;

/**
 * 自定义访问决策
 * 
 * @author Qiujian
 * @date 2019年3月27日
 *
 */
@Component
public class AccessDecisionManagerImpl implements AccessDecisionManager {

	@Autowired
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private ConstProperties constProperties;
	@Autowired
	private DictProperties dictProperties;

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		Dict dict = (Dict) hashOperations.get(constProperties.getDictHash(), dictProperties.getLoginSuccessMsg());
		String msg = dict.getValue();
		// 对没有配置的url 只做登录认证 不做权限控制
		if (configAttributes == null || configAttributes.size() <= 0) {
			return;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			for (ConfigAttribute attr : configAttributes) {
				if (authority.getAuthority().equals(attr.getAttribute())) {
					return;
				}
			}
		}
		throw new AccessDeniedException(msg);
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
