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

import cn.qj.config.listener.ContextStartListener;
import cn.qj.entity.DataDict;
import cn.qj.service.LoginUserServiceImpl;
import cn.qj.util.DictUtil;

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

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String principal = authentication.getPrincipal().toString();
		// 拿到密码错误提示语
		UserDetails userDetails = loginUserServiceImpl.loadUserByUsername(principal);
		DataDict dict = null;
		if (!userDetails.isEnabled()) {
			dict = (DataDict) hashOperations.get(ContextStartListener.DATA_DICT, DictUtil.DISABLED_ERR_MSG);
			throw new DisabledException(dict.getDictValue());
		}
		if (!userDetails.isAccountNonLocked()) {
			dict = (DataDict) hashOperations.get(ContextStartListener.DATA_DICT, DictUtil.LOCKED_ERR_MSG);
			throw new LockedException(dict.getDictValue());
		}
		if (!userDetails.isCredentialsNonExpired()) {
			dict = (DataDict) hashOperations.get(ContextStartListener.DATA_DICT, DictUtil.CREDENTIALS_EXPIRED_ERR_MSG);
			throw new CredentialsExpiredException(dict.getDictValue());
		}
		if (!userDetails.isAccountNonExpired()) {
			dict = (DataDict) hashOperations.get(ContextStartListener.DATA_DICT, DictUtil.ACCOUNT_EXPIRED_ERR_MSG);
			throw new AccountExpiredException(dict.getDictValue());
		}

		// 密码检查
		if (!B_CRYPT.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
			dict = (DataDict) hashOperations.get(ContextStartListener.DATA_DICT, DictUtil.USERNAME_PASSWORD_ERR_MSG);
			throw new BadCredentialsException(dict.getDictValue());
		}
		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(),
				userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
