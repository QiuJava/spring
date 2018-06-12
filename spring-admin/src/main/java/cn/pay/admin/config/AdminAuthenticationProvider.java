package cn.pay.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
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
		try {
			// 调用上层验证逻辑
			Authentication auth = super.authenticate(authentication);
			// 如果验证通过登录成功则重置尝试次数， 否则抛出异常
			return auth;
		} catch (BadCredentialsException e) {
			// 如果验证不通过，则更新尝试次数，当超过次数以后抛出账号锁定异常
			throw e;
		} catch (LockedException e) {
			// 该用户已经被锁定，则进入这个异常
			String error = "账户被锁定";
			throw new LockedException(error);
		}
	}

}
