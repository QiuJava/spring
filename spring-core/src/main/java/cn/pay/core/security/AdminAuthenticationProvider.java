package cn.pay.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 自定义认证
 * 
 * @author Qiujian
 *
 */
@Component
public class AdminAuthenticationProvider extends DaoAuthenticationProvider {

	/**
	 * 注入用户服务 只能用Set方式
	 */
	@Autowired
	@Qualifier("loginInfoService")
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 调用上层验证逻辑
		Authentication auth = super.authenticate(authentication);
		return auth;
	}

}
