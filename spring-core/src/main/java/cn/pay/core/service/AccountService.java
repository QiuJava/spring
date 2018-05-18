package cn.pay.core.service;

import cn.pay.core.domain.business.Account;

/**
 * 账户
 * 
 * @author Administrator
 *
 */
public interface AccountService {

	/**
	 * 获取账户
	 * 
	 * @param id
	 * @return
	 */
	Account get(Long id);

	/**
	 * 更新 并判断乐观锁是否成功
	 * 
	 * @param account
	 */
	void update(Account account);
	
	void save(Account account);

	void checkAccountChange();

}
