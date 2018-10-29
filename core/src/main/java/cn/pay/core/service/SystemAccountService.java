package cn.pay.core.service;

import java.math.BigDecimal;

import cn.pay.core.entity.business.Borrow;
import cn.pay.core.entity.business.SystemAccount;

/**
 * 系统账号服务
 * 
 * @author Administrator
 *
 */
public interface SystemAccountService {

	/**
	 * 收取平台服务费
	 * 
	 * @param borrow
	 * @param serviceCharge
	 */
	void chargeManager(Borrow borrow, BigDecimal serviceCharge);

	/**
	 * 保存和更新 并判断乐观锁是否成功
	 * 
	 * @param systemAccount
	 */
	void update(SystemAccount systemAccount);

	/**
	 * 获取系统账户在数据库中的计数
	 * 
	 * @return
	 */
	Long count();

	/**
	 * 保存
	 * 
	 * @param systemAccount
	 */
	void save(SystemAccount systemAccount);

}
