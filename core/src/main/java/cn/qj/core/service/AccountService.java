package cn.qj.core.service;

import cn.qj.core.entity.Account;

/**
 * 账户服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public interface AccountService {

	/**
	 * 根据用户账户Id获取账户
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

	/**
	 * 保存账户
	 * 
	 * @param account
	 */
	void save(Account account);

	/**
	 * 数据库账户信息防篡改
	 */
	void checkAccountChange();

	/**
	 * 刷新所有用户账户防篡改验证码
	 */
	void flushAccountVerify();

}
