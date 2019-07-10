package cn.loan.core.config.security;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.Permission;
import cn.loan.core.service.LoginUserService;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.ServletContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 用户服务实现
 * 
 * @author qiujian
 *
 */
@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private LoginUserService loginUserService;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HttpServletRequest request = ServletContextUtil.getCurrentHttpRequest();
		String userType = request.getParameter(StringUtil.USER_TYPE).toString();
		LoginUser user = loginUserService.getByUsernameAndUserType(username, Integer.valueOf(userType));
		if (user == null) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}
		// 判断用户是否锁定 锁定状态抛出异常
		// 拿到锁定状态
		Integer lockStatus = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_STATUS,
				SystemDictionaryUtil.USER_STATUS_LOCK, systemDictionaryHashService);
		// 拿到锁定时间
		Integer loginLockingTimeSecond = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.LOGIN_LOCKING,
				SystemDictionaryUtil.LOGIN_LOCKING_TIME_SECOND, systemDictionaryHashService);
		// 并且在锁定时间内
		Date lockingTime = user.getLockingTime();
		if (user.getUserStatus().equals(lockStatus)) {
			if (DateUtil.isOverdue(lockingTime, loginLockingTimeSecond)) {
				// 进行解锁操作
				Integer normalStatus = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_STATUS,
						SystemDictionaryUtil.USER_STATUS_NORMAL, systemDictionaryHashService);
				user.setLockingTime(DateUtil.getNewDate());
				user.setLoginFailureCount(0);
				user.setUserStatus(normalStatus);
				loginUserService.save(user);
			} else {
				long dimSs = DateUtil.dimSs(DateUtil.getNewDate(), lockingTime);
				StringBuilder builder = new StringBuilder();
				builder.append("账户已锁定，请").append(loginLockingTimeSecond - dimSs).append("秒后再试");
				throw new LockedException(builder.toString());
			}
		}
		// 设置权限
		Set<Permission> permissions = user.getPermissions();
		List<Permission> authorities = user.getAuthorities();
		for (Permission permission : permissions) {
			authorities.add(permission);
		}
		return user;
	}

}
