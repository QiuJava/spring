package cn.loan.core.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.Account;
import cn.loan.core.entity.AccountFlow;
import cn.loan.core.entity.Bid;
import cn.loan.core.entity.Borrow;
import cn.loan.core.entity.ReceiptPlan;
import cn.loan.core.entity.Recharge;
import cn.loan.core.entity.RepaymentPlan;
import cn.loan.core.entity.Withdraw;
import cn.loan.core.repository.AccountFlowDao;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 账户流水服务
 * 
 * @author qiujian
 *
 */
@Service
public class AccountFlowService {

	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	@Autowired
	private AccountFlowDao accountFlowDao;

	private AccountFlow createBaseFolw(Account account) {
		AccountFlow accountFlow = new AccountFlow();
		accountFlow.setAccountId(account.getId());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setUsableBalance(account.getUsableBalance());
		accountFlow.setFreezedAmount(account.getFreezedAmount());
		return accountFlow;
	}

	public void bidFLow(Bid bid, Account bidAccount) {
		AccountFlow accountFlow = this.createBaseFolw(bidAccount);
		accountFlow.setAccountId(bidAccount.getId());
		accountFlow.setActionAmount(bid.getBidAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.BID_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(bidAccount.getFreezedAmount());
		accountFlow.setUsableBalance(bidAccount.getUsableBalance());
		accountFlowDao.save(accountFlow);
	}

	public void serviceFeeFlow(BigDecimal serviceFee, Account current) {
		AccountFlow accountFlow = this.createBaseFolw(current);
		accountFlow.setAccountId(current.getId());
		accountFlow.setActionAmount(serviceFee);
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.SERVICE_FEE_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(current.getFreezedAmount());
		accountFlow.setUsableBalance(current.getUsableBalance());
		accountFlowDao.save(accountFlow);
	}

	public void rechargeFolw(Recharge recharge, Account account) {
		AccountFlow accountFlow = this.createBaseFolw(account);
		accountFlow.setAccountId(account.getId());
		accountFlow.setActionAmount(recharge.getAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.RECHARGE_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(account.getFreezedAmount());
		accountFlow.setUsableBalance(account.getUsableBalance());
		accountFlow.setRemark(recharge.getRemark());
		accountFlowDao.save(accountFlow);
	}

	public void borrowPostFlow(Borrow borrow, Account borrowAccount) {
		AccountFlow accountFlow = this.createBaseFolw(borrowAccount);
		accountFlow.setAccountId(borrowAccount.getId());
		accountFlow.setActionAmount(borrow.getBorrowAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.BORROW_POST_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(borrowAccount.getFreezedAmount());
		accountFlow.setUsableBalance(borrowAccount.getUsableBalance());
		accountFlowDao.save(accountFlow);
	}

	public void cancelBidFlow(Bid bid, Account bidAccount) {
		AccountFlow accountFlow = this.createBaseFolw(bidAccount);
		accountFlow.setAccountId(bidAccount.getId());
		accountFlow.setActionAmount(bid.getBidAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.CANCEL_BID_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(bidAccount.getFreezedAmount());
		accountFlow.setUsableBalance(bidAccount.getUsableBalance());
		accountFlowDao.save(accountFlow);
	}

	public void bidSuccessFlow(Bid bid, Account bidAccount) {
		AccountFlow accountFlow = this.createBaseFolw(bidAccount);
		accountFlow.setAccountId(bidAccount.getId());
		accountFlow.setActionAmount(bid.getBidAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.BID_SUCCESS_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(bidAccount.getFreezedAmount());
		accountFlow.setUsableBalance(bidAccount.getUsableBalance());
		accountFlowDao.save(accountFlow);
	}

	public void repaymentFlow(RepaymentPlan plan, Account borrowAccount) {
		AccountFlow accountFlow = this.createBaseFolw(borrowAccount);
		accountFlow.setAccountId(borrowAccount.getId());
		accountFlow.setActionAmount(plan.getTotalAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.REPAYMENT_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(borrowAccount.getFreezedAmount());
		accountFlow.setUsableBalance(borrowAccount.getUsableBalance());
		accountFlowDao.save(accountFlow);

	}

	public void receiptFlow(ReceiptPlan receiptPlan, Account bidAccount) {
		AccountFlow accountFlow = this.createBaseFolw(bidAccount);
		accountFlow.setAccountId(bidAccount.getId());
		accountFlow.setActionAmount(receiptPlan.getTotalAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.RECEIPT_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(bidAccount.getFreezedAmount());
		accountFlow.setUsableBalance(bidAccount.getUsableBalance());
		accountFlowDao.save(accountFlow);
	}

	public void withdrawApplyFlow(Withdraw withdraw, Account account) {
		AccountFlow accountFlow = this.createBaseFolw(account);
		accountFlow.setAccountId(account.getId());
		accountFlow.setActionAmount(withdraw.getAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.WITHDRAW_APPLY_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(account.getFreezedAmount());
		accountFlow.setUsableBalance(account.getUsableBalance());
		accountFlowDao.save(accountFlow);

	}

	public void withdrawSuccessFlow(Withdraw withdraw, Account currentAccount) {
		AccountFlow accountFlow = this.createBaseFolw(currentAccount);
		accountFlow.setAccountId(currentAccount.getId());
		accountFlow.setActionAmount(withdraw.getAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.WITHDRAW_SUCCESS_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(currentAccount.getFreezedAmount());
		accountFlow.setUsableBalance(currentAccount.getUsableBalance());
		accountFlowDao.save(accountFlow);
	}

	public void withdrawFailedFlow(Withdraw withdraw, Account currentAccount) {
		AccountFlow accountFlow = this.createBaseFolw(currentAccount);
		accountFlow.setAccountId(currentAccount.getId());
		accountFlow.setActionAmount(withdraw.getAmount());
		accountFlow.setActionTime(DateUtil.getNewDate());
		accountFlow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.WITHDRAW_FAILED_ACTION, systemDictionaryHashService));
		accountFlow.setFreezedAmount(currentAccount.getFreezedAmount());
		accountFlow.setUsableBalance(currentAccount.getUsableBalance());
		accountFlowDao.save(accountFlow);

	}

}
