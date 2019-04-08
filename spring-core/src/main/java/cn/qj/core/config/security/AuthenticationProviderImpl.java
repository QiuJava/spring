package cn.qj.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.service.DataDictService;
import cn.qj.core.service.LoginUserServiceImpl;
import cn.qj.core.util.DictUtil;

/**
 * 认证供应实现
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

	public static final BCryptPasswordEncoder B_CRYPT = new BCryptPasswordEncoder();

	@Autowired
	private LoginUserServiceImpl loginUserServiceImpl;

	@Autowired
	private DataDictService dataDictService;

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String principal = authentication.getPrincipal().toString();
		// 拿到密码错误提示语
		String errMag = dataDictService.getDictValueByDictkey(DictUtil.USERNAME_PASSWORD_ERR_MSG);
		UserDetails userDetails = loginUserServiceImpl.loadUserByUsername(principal);
		if (userDetails == null) {
			throw new UsernameNotFoundException(errMag);
		} else {
			if (!userDetails.isEnabled()) {
				throw new DisabledException(errMag);
			}
			if (!userDetails.isAccountNonLocked()) {
				throw new LockedException(errMag);
			}
			if (!userDetails.isCredentialsNonExpired()) {
				throw new CredentialsExpiredException(errMag);
			}
			if (!userDetails.isAccountNonExpired()) {
				throw new AccountExpiredException(errMag);
			}
		}

		// 密码检查
		if (!B_CRYPT.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
			throw new BadCredentialsException(errMag);
		}
		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(),
				userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
