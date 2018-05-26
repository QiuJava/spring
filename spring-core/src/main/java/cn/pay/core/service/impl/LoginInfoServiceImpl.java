package cn.pay.core.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.pay.core.consts.BidConst;
import cn.pay.core.dao.LoginInfoRepository;
import cn.pay.core.domain.business.Account;
import cn.pay.core.domain.business.UserInfo;
import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.service.AccountService;
import cn.pay.core.service.IpLogService;
import cn.pay.core.service.LoginInfoService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.DateUtil;
import cn.pay.core.util.HttpSessionContext;
import cn.pay.core.util.LogicException;
import cn.pay.core.util.Md5;

@Service
public class LoginInfoServiceImpl implements LoginInfoService, UserDetailsService {

	@Autowired
	private LoginInfoRepository repository;

	@Autowired
	private IpLogService ipLogService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserInfoService userInfoService;

	@Override
	@Transactional
	public LoginInfo login(String username, String password, String ip, Integer userType) {
		// 登录名检查
		LoginInfo loginInfo = repository.findByUsername(username);
		if (loginInfo == null || loginInfo.getStatus() == LoginInfo.DEL) {
			throw new LogicException("用户名不存在！");
		}

		// 初始化
		if (loginInfo.getStatus() == LoginInfo.LOCK
				&& System.currentTimeMillis() - loginInfo.getLockTime().getTime() >= DateUtil.LOCK_TIME) {
			loginInfo.setLoserCount(0);
			loginInfo.setStatus(LoginInfo.NORMAL);
			loginInfo.setLockTime(null);
			repository.saveAndFlush(loginInfo);
		}
		if (loginInfo.getLoserCount() == LoginInfo.LOSER_MAX_COUNT) {
			StringBuilder errMsg = new StringBuilder();
			errMsg.append("密码输错").append(LoginInfo.LOSER_MAX_COUNT).append("次，请").append(
					((loginInfo.getLockTime().getTime() + DateUtil.LOCK_TIME) - System.currentTimeMillis()) / 1000)
					.append("秒后再进行登录");
			throw new LogicException(errMsg.toString());
		}
		// 记录登录日志
		IpLog ipLog = new IpLog();
		ipLog.setUsername(username);
		ipLog.setIp(ip);
		ipLog.setUserType(userType);
		ipLog.setLoginTime(new Date());
		// 状态检查
		if (loginInfo.getStatus() == LoginInfo.NORMAL) {
			LoginInfo current = repository.findByUsernameAndPasswordAndUserType(username, Md5.encode(password),
					userType);
			if (current != null) {
				HttpSessionContext.setCurrentLoginInfo(current);
				// 只有登陆成功才记录
				ipLog.setLoginState(IpLog.SUCCESS);
				ipLogService.saveAndUpdate(ipLog);
				// 登录成功清空登录失败次数 和 锁定时间
				current.setLoserCount(0);
				current.setLockTime(null);
				repository.saveAndFlush(current);
				return current;
			} else { // 账号或密码错误
				loginInfo.setLoserCount(loginInfo.getLoserCount() + 1);
				repository.saveAndFlush(loginInfo);
			}
			if (loginInfo.getLoserCount() == LoginInfo.LOSER_MAX_COUNT) {
				// 达到次数进行锁定
				loginInfo.setStatus(LoginInfo.LOCK);
				loginInfo.setLockTime(new Date());
				repository.saveAndFlush(loginInfo);
			}
		}
		ipLog.setLoginState(IpLog.FAIL);
		ipLogService.saveAndUpdate(ipLog);
		return null;
	}

	@Override
	@Transactional
	public void register(String username, String password) {
		LoginInfo loginInfo = repository.findByUsername(username);
		if (loginInfo != null) {
			throw new LogicException("用户名已存在");
		} else {
			if (!StringUtils.hasLength(username)) {
				throw new LogicException("请输入用户名");
			}
			if (!StringUtils.hasLength(password)) {
				throw new LogicException("请输入密码");
			}

			loginInfo = new LoginInfo();
			loginInfo.setUsername(username);
			loginInfo.setPassword(Md5.encode(password));
			loginInfo.setStatus(LoginInfo.NORMAL);
			LoginInfo info = repository.saveAndFlush(loginInfo);

			// 注册账号时创建额外的对象 账户 和 用户信息
			Account account = new Account();
			account.setId(info.getId());
			account.setBorrowLimit(BidConst.INIT_BORROW_LIMIT);
			account.setRemainBorrowLimit(BidConst.INIT_BORROW_LIMIT);
			account.setTradePassword(BidConst.INIT_TRADE_PASSWORD);
			accountService.save(account);

			UserInfo userInfo = new UserInfo();
			userInfo.setId(info.getId());
			userInfoService.save(userInfo);
		}

	}

	@Override
	public boolean isExist(String username) {
		LoginInfo loginInfo = repository.findByUsername(username);
		if (loginInfo == null) {
			return true;
		}
		return false;
	}

	@Override
	public LoginInfo get(Long id) {
		return repository.findOne(id);
	}

	@Override
	public LoginInfo getByUsername(String username) {
		return repository.findByUsername(username);
	}

	@Override
	@Transactional
	public void saveAndUpdate(LoginInfo info) {
		repository.saveAndFlush(info);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
