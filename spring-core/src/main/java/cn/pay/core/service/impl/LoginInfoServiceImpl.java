package cn.pay.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.pay.core.consts.BidConst;
import cn.pay.core.dao.LoginInfoRepository;
import cn.pay.core.domain.business.Account;
import cn.pay.core.domain.business.UserInfo;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.domain.sys.Role;
import cn.pay.core.service.AccountService;
import cn.pay.core.service.LoginInfoService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.LogicException;

@Service("loginInfoService")
public class LoginInfoServiceImpl implements LoginInfoService {

	@Autowired
	private LoginInfoRepository repository;

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserInfoService userInfoService;

	@Override
	@Transactional
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
	public boolean isExist(String username) {
		return repository.countByUsername(username) > 0;
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
	@CacheEvict(value = { "loadUserByUsername" }, allEntries = true)
	public void saveAndUpdate(LoginInfo info) {
		repository.saveAndFlush(info);
	}

	@Cacheable("loadUserByUsername")
	@Transactional
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
}
