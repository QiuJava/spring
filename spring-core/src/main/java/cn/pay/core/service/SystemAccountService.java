package cn.pay.core.service;

import java.math.BigDecimal;

import cn.pay.core.domain.business.Borrow;
import cn.pay.core.domain.business.SystemAccount;

/**
 * 系统账号
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

	Long count();

	void save(SystemAccount systemAccount);

}
