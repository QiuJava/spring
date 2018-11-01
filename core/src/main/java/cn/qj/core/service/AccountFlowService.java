package cn.qj.core.service;

import java.math.BigDecimal;

import cn.qj.core.entity.Account;
import cn.qj.core.entity.Bid;
import cn.qj.core.entity.Borrow;
import cn.qj.core.entity.PaymentPlan;
import cn.qj.core.entity.Recharge;
import cn.qj.core.entity.RepaymentSchedule;
import cn.qj.core.entity.Withdraw;

/**
 * 账户流水
 * 
 * @author Qiujian
 * @date 2018/11/01
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
