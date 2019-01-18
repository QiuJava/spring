package cn.qj.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.common.LogicException;
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
			LoginInfo loginInfo = new LoginInfo();
			Date date = new Date();
			loginInfo.setUsername(username);
			loginInfo.setPassword(new BCryptPasswordEncoder().encode(password));
			loginInfo.setGmtCreate(date);
			loginInfo.setGmtModified(date);
			LoginInfo info = repository.saveAndFlush(loginInfo);

			// 注册账号时创建额外的对象 账户 和 用户信息
			Account account = new Account();
			account.setId(info.getId());
			account.setVerifyKey(account.getVerifyKey());
			accountService.save(account);

			UserInfo userInfo = new UserInfo();
			userInfo.setId(info.getId());
			userInfoService.save(userInfo);
		}

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
	public boolean isExistAdmin(int userType) {
		return repository.countByUserType(userType) > 0;
	}

	@Override
	public boolean isExistByUsername(String username) {
		return repository.countByUsername(username) > 0;
	}
}
