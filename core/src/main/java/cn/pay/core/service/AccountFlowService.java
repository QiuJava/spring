package cn.pay.core.service;

import java.math.BigDecimal;

import cn.pay.core.domain.business.Account;
import cn.pay.core.domain.business.Bid;
import cn.pay.core.domain.business.Borrow;
import cn.pay.core.domain.business.PaymentPlan;
import cn.pay.core.domain.business.Recharge;
import cn.pay.core.domain.business.RepaymentSchedule;
import cn.pay.core.domain.business.Withdraw;

/**
 * 账户流水服务
 * 
 * @author Administrator
 *
 */
public interface AccountFlowService {

	/**
	 * 线下充值流水
	 * 
	 * @param recharge
	 * @param account
	 */
	void rechargeFolw(Recharge recharge, Account account);

	/**
	 * 提现审核流水
	 * 
	 * @param withdraw
	 * @param account
	 */
	void withdrawApplyFlow(Withdraw withdraw, Account account);

	/**
	 * 提现成功流水
	 * 
	 * @param withdraw
	 * @param currentAccount
	 */
	void withdrawSuccessFlow(Withdraw withdraw, Account currentAccount);

	/**
	 * 提现失败流水
	 * 
	 * @param withdraw
	 * @param currentAccount
	 */
	void withdrawLoseFlow(Withdraw withdraw, Account currentAccount);

	/**
	 * 投标流水
	 * 
	 * @param bid
	 * @param bidAccount
	 */
	void bidFLow(Bid bid, Account bidAccount);

	/**
	 * 满标一审 满标二审拒绝流水
	 * 
	 * @param bid
	 * @param bidAccount
	 */
	void cancelBorrowFlow(Bid bid, Account bidAccount);

	/**
	 * 借款流水
	 * 
	 * @param borrow
	 * @param borrowAccount
	 */
	void borrowFlow(Borrow borrow, Account borrowAccount);

	/**
	 * 平台服务费流水
	 * 
	 * @param serviceCharge
	 * @param borrowAccount
	 * @param borrow
	 */
	void serviceChargeFlow(BigDecimal serviceCharge, Account borrowAccount, Borrow borrow);

	/**
	 * 投标成功流水
	 * 
	 * @param b
	 * @param bidAccount
	 */
	void bidSuccessFlow(Bid b, Account bidAccount);

	/**
	 * 还款流水
	 * 
	 * @param rs
	 * @param currentAccount
	 */
	void repayFlow(RepaymentSchedule rs, Account currentAccount);

	/**
	 * 收款流水
	 * 
	 * @param pp
	 * @param bidAccount
	 */
	void receiptFlow(PaymentPlan pp, Account bidAccount);

}
