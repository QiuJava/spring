package cn.qj.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.qj.core.common.LogicException;
import cn.qj.core.consts.BidConst;
import cn.qj.core.entity.Account;
import cn.qj.core.entity.LoginInfo;
import cn.qj.core.entity.Role;
import cn.qj.core.entity.UserInfo;
import cn.qj.core.repository.LoginInfoRepository;
import cn.qj.core.service.AccountService;
import cn.qj.core.service.LoginInfoService;
import cn.qj.core.service.UserInfoService;

/**
 * 登录信息服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
@Service("loginInfoService")
public class LoginInfoServiceImpl implements LoginInfoService {

	@Autowired
	private LoginInfoRepository repository;

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserInfoService userInfoService;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void register(String username, String password) {
		if (repository.countByUsername(username) > 0) {
			throw new LogicException("用户名已存在");
		} else {
			if (!StringUtils.hasLength(username)) {
				throw new LogicException("请输入用户名");
			}
			if (!StringUtils.hasLength(password)) {
				throw new LogicException("请输入密码");
			}

			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setUsername(username);
			loginInfo.setPassword(new BCryptPasswordEncoder().encode(password));
			loginInfo.setStatus(LoginInfo.NORMAL);
			LoginInfo info = repository.saveAndFlush(loginInfo);

			// 注册账号时创建额外的对象 账户 和 用户信息
			Account account = new Account();
			account.setId(info.getId());
			account.setBorrowLimit(BidConst.INIT_BORROW_LIMIT);
			account.setRemainBorrowLimit(BidConst.INIT_BORROW_LIMIT);
			accountService.save(account);

			UserInfo userInfo = new UserInfo();
			userInfo.setId(info.getId());
			userInfoService.save(userInfo);
		}

	}

	@Override
	public Boolean isExistByUsername(String username) {
		return repository.countByUsername(username) > 0;
	}

	@Override
	public LoginInfo getLoginInfoById(Long id) {
		return repository.findOne(id);
	}

	@Override
	public LoginInfo getLoginInfoByUsername(String username) {
		return repository.findByUsername(username);
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginInfo loginInfo = repository.findByUsername(username);
		if (loginInfo != null) {
			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			for (Role role : loginInfo.getRoles()) {
				// 获取用户所拥有的权限
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
				grantedAuthorities.add(grantedAuthority);
			}
			loginInfo.setAuthorities(grantedAuthorities);
			return loginInfo;
		} else {
			throw new UsernameNotFoundException("用户名: " + username + " 不存在!");
		}
	}

	@Override
	public List<LoginInfo> listAll() {
		return repository.findAll();
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public LoginInfo updateLoginInfo(LoginInfo loginInfo) {
		return repository.saveAndFlush(loginInfo);
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	@Override
	public LoginInfo saveLoginInfo(LoginInfo loginInfo) {
		return repository.saveAndFlush(loginInfo);
	}

	@Override
	public Boolean isExistAdmin(boolean isAdmin) {
		return repository.countByIsAdmin(isAdmin) > 0;
	}
}
