package cn.qj.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import cn.qj.config.properties.ConstProperties;
import cn.qj.config.properties.DictProperties;
import cn.qj.entity.Dict;
import cn.qj.service.LoginUserServiceImpl;

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
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private ConstProperties constProperties;
	@Autowired
	private DictProperties dictProperties;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String principal = authentication.getPrincipal().toString();
		// 拿到密码错误提示语
		UserDetails userDetails = loginUserServiceImpl.loadUserByUsername(principal);
		Dict dict = null;
		String dictHash = constProperties.getDictHash();
		if (!userDetails.isEnabled()) {
			dict = (Dict) hashOperations.get(dictHash, dictProperties.getDisabledErrMsg());
			throw new DisabledException(dict.getValue());
		}
		if (!userDetails.isAccountNonLocked()) {
			dict = (Dict) hashOperations.get(dictHash, dictProperties.getLockedErrMsg());
			throw new LockedException(dict.getValue());
		}
		if (!userDetails.isCredentialsNonExpired()) {
			dict = (Dict) hashOperations.get(dictHash, dictProperties.getCredentialsExpiredErrMsg());
			throw new CredentialsExpiredException(dict.getValue());
		}
		if (!userDetails.isAccountNonExpired()) {
			dict = (Dict) hashOperations.get(dictHash, dictProperties.getAccountExpiredErrMsg());
			throw new AccountExpiredException(dict.getValue());
		}

		// 密码检查
		if (!B_CRYPT.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
			dict = (Dict) hashOperations.get(dictHash, dictProperties.getUsernamePasswordErrMsg());
			throw new BadCredentialsException(dict.getValue());
		}
		// 获取用户菜单

		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), userDetails,
				userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
