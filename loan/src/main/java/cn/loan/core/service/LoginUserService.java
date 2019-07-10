package cn.loan.core.service;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.LogicException;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.Account;
import cn.loan.core.entity.LoginLog;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.repository.LoginUserDao;
import cn.loan.core.util.BigDecimalUtil;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 登录用户相关服务
 * 
 * @author qiujian
 *
 */
@Service
public class LoginUserService {

	@Autowired
	private LoginUserDao loginUserDao;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public boolean existUser() {
		return loginUserDao.count() > 0;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(LoginUser loginUser) {
		loginUserDao.save(loginUser);
	}

	public LoginUser getByUsername(String username) {
		return loginUserDao.findByUsername(username);
	}

	public boolean existUserByUserType(Integer userType) {
		return loginUserDao.countByUserType(userType) > 0;
	}

	public boolean existUserByUsername(String username) {
		return loginUserDao.countByUsername(username) > 0;
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	public void register(String username, String password) {
		if (this.existUserByUsername(username)) {
			throw new LogicException("用户名已存在");
		} else {
			LoginUser loginUser = new LoginUser();
			Integer userType = Integer.valueOf(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_TYPE,
					SystemDictionaryUtil.USER_TYPE_WEBSITE, systemDictionaryHashService));
			loginUser.setUsername(username);
			loginUser.setPassword(bCryptPasswordEncoder.encode(password));
			
			Integer normal = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_STATUS,
					SystemDictionaryUtil.USER_STATUS_NORMAL, systemDictionaryHashService);
			loginUser.setUserType(userType);
			loginUser.setUserStatus(normal);
			loginUser.setLoginFailureCount(0);
			loginUser.setLockingTime(DateUtil.getNewDate());
			this.save(loginUser);

			// 注册账号时创建额外的对象 账户 和 用户信息
			Account account = new Account();
			account.setId(loginUser.getId());
			account.setBorrowLimit(BigDecimalUtil.ZERO);
			account.setFreezedAmount(BigDecimalUtil.ZERO);
			Integer initBorrowLimit = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.INIT,
					SystemDictionaryUtil.INIT_BORROW_LIMIT, systemDictionaryHashService);
			account.setRemainBorrowLimit(new BigDecimal(initBorrowLimit));
			account.setUnReceiveInterest(BigDecimalUtil.ZERO);
			account.setUnReceivePrincipal(BigDecimalUtil.ZERO);
			account.setUnReturnAmount(BigDecimalUtil.ZERO);
			account.setUsableBalance(BigDecimalUtil.ZERO);

			UserInfo userInfo = new UserInfo();
			userInfo.setId(loginUser.getId());
			userInfo.setAuthScore(0);
			userInfo.setStatusValue(0L);

			accountService.save(account);
			userInfoService.save(userInfo);
		}

	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void loginFailure(HttpServletRequest request) {
		String username = request.getParameter(StringUtil.USERNAME).toString();
		LoginUser user = this.getByUsername(username);
		// 用户名不存在则不记录
		if (user != null) {
			// 记录登录日志 并记录登录失败
			LoginLog log = new LoginLog();
			log.setIpAddress(request.getRemoteAddr());
			Integer loginStatus = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.LOGIN_STATUS,
					SystemDictionaryUtil.LOGIN_STATUS_FAILURE, systemDictionaryHashService);
			log.setLoginStatus(loginStatus);
			log.setUsername(username);
			Integer failureCount = user.getLoginFailureCount();
			user.setLoginFailureCount(++failureCount);
			// 获取失败次数
			Integer loginLockingCount = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.LOGIN_LOCKING,
					SystemDictionaryUtil.LOGIN_LOCKING_COUNT, systemDictionaryHashService);
			Integer lockStatus = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_STATUS,
					SystemDictionaryUtil.USER_STATUS_LOCK, systemDictionaryHashService);
			if (failureCount >= loginLockingCount && user.getUserStatus() != lockStatus) {
				// 进入锁定状态
				user.setUserStatus(lockStatus);
				user.setLockingTime(DateUtil.getNewDate());
			}
			// 更新用户
			this.save(user);
			loginLogService.save(log);
		}
	}

	public LoginUser getByUsernameAndUserType(String username, Integer userType) {
		return loginUserDao.findByUsernameAndUserType(username, userType);
	}

}
