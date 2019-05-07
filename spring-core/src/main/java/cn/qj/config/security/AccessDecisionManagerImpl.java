package cn.qj.config.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import cn.qj.config.listener.ContextStartListener;
import cn.qj.entity.DataDict;
import cn.qj.util.DictUtil;

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

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		DataDict dict = (DataDict) hashOperations.get(ContextStartListener.DATA_DICT, DictUtil.NO_RIGHTS_MSG);
		String msg = dict.getDictValue();
		if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
			throw new InsufficientAuthenticationException(msg);
		}
		// 对所有url进行权限认证
		if (configAttributes == null || configAttributes.size() <= 0) {
			throw new AccessDeniedException(msg);
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
