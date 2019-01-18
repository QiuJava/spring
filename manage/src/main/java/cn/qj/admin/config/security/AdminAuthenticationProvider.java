package cn.qj.admin.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import cn.qj.core.consts.SysConst;
import cn.qj.core.entity.LoginInfo;
import cn.qj.core.service.LoginInfoService;

/**
 * 自定义认证
 * 
 * @author Qiujian
 * @date 2018/8/13
 */
@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private LoginInfoService loginInfoService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		// 页面输入的密码
		String password = (String) authentication.getCredentials();
		LoginInfo loginInfo = (LoginInfo) loginInfoService.loadUserByUsername(username);

		if (!loginInfo.isAccountNonLocked()) {
			long lockTimeLong = loginInfo.getLockTime().getTime();
			// 锁定时间过了 恢复为正常状态
			if (System.currentTimeMillis() - lockTimeLong >= LoginInfo.LOCK_TIME) {
				loginInfo.setStatus(LoginInfo.NORMAL);
			} else {
				Long seconds = (((lockTimeLong + LoginInfo.LOCK_TIME) - System.currentTimeMillis()) / 1000);

				StringBuilder errMsgStr = new StringBuilder();
				errMsgStr.append("密码输错");
				errMsgStr.append(SysConst.LOSER_MAX_COUNT);
				errMsgStr.append("次，请");
				errMsgStr.append(seconds.toString());
				errMsgStr.append("秒后再进行登录");
				throw new LockedException(errMsgStr.toString());
			}
		}

		// 密码匹配
		if (!new BCryptPasswordEncoder().matches(password, loginInfo.getPassword())) {
			throw new BadCredentialsException("密码错误");
		}

		// 登录失败次数清0
		loginInfo.setLoserCount(0);
		loginInfo.setLockTime(null);
		loginInfo.setGmtModified(new Date());
		loginInfoService.saveLoginInfo(loginInfo);

		return new UsernamePasswordAuthenticationToken(loginInfo, password, loginInfo.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
