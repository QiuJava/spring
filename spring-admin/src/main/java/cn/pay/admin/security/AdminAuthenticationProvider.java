package cn.pay.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.service.LoginInfoService;
import cn.pay.core.util.DateUtil;

/**
 * 自定义认证
 * 
 * @author Qiujian
 *
 */
@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private LoginInfoService loginInfoService;

	@Override
	@Transactional
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		LoginInfo loginInfo = (LoginInfo) loginInfoService.loadUserByUsername(username);

		// 初始化
		if (!loginInfo.isAccountNonLocked()
				&& System.currentTimeMillis() - loginInfo.getLockTime().getTime() >= DateUtil.LOCK_TIME) {
			// loginInfo.setLoserCount(0);
			loginInfo.setStatus(LoginInfo.NORMAL);
			// loginInfo.setLockTime(null);
			// loginInfoService.saveAndUpdate(loginInfo);
		}

		if (!loginInfo.isAccountNonLocked()) {
			StringBuilder errMsg = new StringBuilder();
			Long seconds = (((loginInfo.getLockTime().getTime() + DateUtil.LOCK_TIME) - System.currentTimeMillis())
					/ 1000);
			errMsg.append("密码输错")//
					.append(LoginInfo.LOSER_MAX_COUNT).append("次，请")//
					.append(seconds.toString())//
					.append("秒后再进行登录");//
			throw new LockedException(errMsg.toString());
		}

		// 密码匹配
		if (!new BCryptPasswordEncoder().matches(password, loginInfo.getPassword())) {
			throw new BadCredentialsException("密码错误");
		}

		loginInfo.setLoserCount(0);
		loginInfo.setLockTime(null);
		loginInfoService.saveAndUpdate(loginInfo);
		return new UsernamePasswordAuthenticationToken(loginInfo, password, loginInfo.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
