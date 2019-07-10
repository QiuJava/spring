package cn.loan.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.entity.Account;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.repository.AccountDao;
import cn.loan.core.util.SecurityContextUtil;

/**
 * 账户服务
 * 
 * @author qiujian
 *
 */
@Service
public class AccountService {

	@Autowired
	private AccountDao accountDao;

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(Account account) {
		accountDao.save(account);
	}

	public Account get(Long id) {
		return accountDao.findOne(id);
	}

	public Account getCurrent() {
		LoginUser user = SecurityContextUtil.getCurrentUser();
		return accountDao.findOne(user.getId());
	}

	public void save(List<Account> bidAccounts) {
		accountDao.save(bidAccounts);
	}

}
