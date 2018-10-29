package cn.pay.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.consts.BidConst;
import cn.pay.core.entity.business.Account;
import cn.pay.core.entity.business.AccountFlow;
import cn.pay.core.entity.business.Bid;
import cn.pay.core.entity.business.Borrow;
import cn.pay.core.entity.business.PaymentPlan;
import cn.pay.core.entity.business.Recharge;
import cn.pay.core.entity.business.RepaymentSchedule;
import cn.pay.core.entity.business.Withdraw;
import cn.pay.core.repository.AccountFlowRepository;
import cn.pay.core.service.AccountFlowService;

/**
 * 账户流水服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
@Transactional(rollbackFor = { RuntimeException.class })
public class AccountFlowServiceImpl implements AccountFlowService {

	@Autowired
	private AccountFlowRepository repository;

	private AccountFlow createBaseFolw(Account account) {
		AccountFlow accountFlow = new AccountFlow();
		accountFlow.setAccountId(account.getId());
		accountFlow.setActionTime(new Date());
		accountFlow.setBalance(account.getUsableAmount());
		accountFlow.setFreezed(account.getFreezedAmount());
		return accountFlow;
	}

	@Override
	public void rechargeFolw(Recharge recharge, Account account) {
		AccountFlow accountFlow = createBaseFolw(account);
		accountFlow.setAmount(recharge.getAmount());
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_DEPOSIT_OFFLINE_LOCAL);
		accountFlow.setNote("线下充值成功！");
		accountFlow.setRemark(recharge.getRemark());
		repository.saveAndFlush(accountFlow);
	}

	@Override
	public void withdrawApplyFlow(Withdraw withdraw, Account account) {
		AccountFlow accountFlow = createBaseFolw(account);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED);
		accountFlow.setNote("提现申请冻结金额");
		accountFlow.setAmount(withdraw.getMoneyAmount());
		accountFlow.setRemark(withdraw.getRemark());
		repository.saveAndFlush(accountFlow);
	}

	@Override
	public void withdrawSuccessFlow(Withdraw withdraw, Account currentAccount) {
		AccountFlow accountFlow = createBaseFolw(currentAccount);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW);
		accountFlow.setNote("提现成功");
		accountFlow.setAmount(withdraw.getMoneyAmount());
		accountFlow.setRemark(withdraw.getRemark());
		repository.saveAndFlush(accountFlow);

	}

	@Override
	public void withdrawLoseFlow(Withdraw withdraw, Account currentAccount) {
		AccountFlow accountFlow = createBaseFolw(currentAccount);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED);
		accountFlow.setNote("提现申请失败");
		accountFlow.setAmount(withdraw.getMoneyAmount());
		accountFlow.setRemark(withdraw.getRemark());
		repository.saveAndFlush(accountFlow);
	}

	@Override
	public void bidFLow(Bid bid, Account bidAccount) {
		AccountFlow accountFlow = createBaseFolw(bidAccount);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL);
		accountFlow.setNote("投标:" + bid.getBorrowTitle() + "成功");
		accountFlow.setAmount(bid.getAmount());
		repository.saveAndFlush(accountFlow);
	}

	@Override
	public void cancelBorrowFlow(Bid bid, Account bidAccount) {
		AccountFlow accountFlow = createBaseFolw(bidAccount);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED);
		accountFlow.setNote("借款" + bid.getBorrowTitle() + "的审核失败,取消投标冻结金额");
		accountFlow.setAmount(bid.getAmount());
		repository.saveAndFlush(accountFlow);
	}

	@Override
	public void borrowFlow(Borrow borrow, Account borrowAccount) {
		AccountFlow accountFlow = createBaseFolw(borrowAccount);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED);
		accountFlow.setNote("借款" + borrow.getTitle() + "借款成功");
		accountFlow.setAmount(borrow.getAmount());
		repository.saveAndFlush(accountFlow);
	}

	@Override
	public void serviceChargeFlow(BigDecimal serviceCharge, Account borrowAccount, Borrow borrow) {
		AccountFlow accountFlow = createBaseFolw(borrowAccount);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_CHARGE);
		accountFlow.setNote("借款" + borrow.getTitle() + "支付平台服务费成功");
		accountFlow.setAmount(serviceCharge);
		repository.saveAndFlush(accountFlow);

	}

	@Override
	public void bidSuccessFlow(Bid b, Account bidAccount) {
		AccountFlow accountFlow = createBaseFolw(bidAccount);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL);
		accountFlow.setNote("借款" + b.getBorrowTitle() + "投标成功");
		accountFlow.setAmount(b.getAmount());
		repository.saveAndFlush(accountFlow);
	}

	@Override
	public void repayFlow(RepaymentSchedule rs, Account currentAccount) {
		AccountFlow accountFlow = createBaseFolw(currentAccount);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY);
		accountFlow.setNote("第" + rs.getMonthIndex() + "期还款成功");
		accountFlow.setAmount(rs.getTotalAmount());
		repository.saveAndFlush(accountFlow);
	}

	@Override
	public void receiptFlow(PaymentPlan pp, Account bidAccount) {
		AccountFlow accountFlow = createBaseFolw(bidAccount);
		accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY);
		accountFlow.setNote("第" + pp.getMonthIndex() + "期收款成功");
		accountFlow.setAmount(pp.getTotalAmount());
		repository.saveAndFlush(accountFlow);
	}

}
