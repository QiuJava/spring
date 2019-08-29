package cn.loan.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.LogicException;
import cn.loan.core.common.PageResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.Account;
import cn.loan.core.entity.Bid;
import cn.loan.core.entity.Borrow;
import cn.loan.core.entity.BorrowAuditHistroy;
import cn.loan.core.entity.ReceiptPlan;
import cn.loan.core.entity.RepaymentPlan;
import cn.loan.core.entity.SystemAccount;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.entity.bo.PerMonth;
import cn.loan.core.entity.qo.BorrowQo;
import cn.loan.core.repository.BorrowDao;
import cn.loan.core.repository.specification.BorrowSpecification;
import cn.loan.core.util.BigDecimalUtil;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;
import cn.loan.core.util.UserInfoStatusUtil;

/**
 * 借款服务
 * 
 * @author qiujian
 *
 */
@Service
public class BorrowService {
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private BorrowDao borrowDao;

	@Autowired
	private AccountService accountService;
	@Autowired
	private SystemAccountService systemAccountService;
	@Autowired
	private AccountFlowService accountFlowService;

	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;
	@Autowired
	private BorrowAuditHistroyService borrowAuditHistroyService;

	@Autowired
	private SystemAccountFlowService systemAccountFlowService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private ReceiptPlanService receiptPlanService;

	@Transactional(rollbackFor = RuntimeException.class)
	public void apply(Borrow borrow) {
		BigDecimal minBorrowRate = SystemDictionaryUtil.getItemValueBigDecimal(SystemDictionaryUtil.INIT,
				SystemDictionaryUtil.INIT_MIN_BORROW_RATE, systemDictionaryHashService);
		BigDecimal maxBorrowRate = SystemDictionaryUtil.getItemValueBigDecimal(SystemDictionaryUtil.INIT,
				SystemDictionaryUtil.INIT_MAX_BORROW_RATE, systemDictionaryHashService);

		Integer monthInstalment = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.REPAYMENT_METHOD,
				SystemDictionaryUtil.MONTH_INSTALMENT, systemDictionaryHashService);
		Integer monthExpire = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.REPAYMENT_METHOD,
				SystemDictionaryUtil.MONTH_EXPIRE, systemDictionaryHashService);

		BigDecimal serviceFeeRate = SystemDictionaryUtil.getItemValueBigDecimal(SystemDictionaryUtil.INIT,
				SystemDictionaryUtil.INIT_SERVICE_FEE_RATE, systemDictionaryHashService);

		Integer repaymentMethod = borrow.getRepaymentMethod();
		BigDecimal borrowAmount = borrow.getBorrowAmount();
		BigDecimal rate = borrow.getRate();
		Integer repaymentMonths = borrow.getRepaymentMonths();
		// 扣除服务费
		BigDecimal serviceFee = BigDecimalUtil.ZERO;
		if (monthInstalment.equals(repaymentMethod)) {
			serviceFee = BigDecimalUtil.getTotalInterest(borrowAmount, serviceFeeRate, repaymentMonths);
		} else if (monthExpire.equals(repaymentMethod)) {
			serviceFee = BigDecimalUtil.getTotalInterests(borrowAmount, serviceFeeRate, repaymentMonths);
		}
		Account current = accountService.getCurrent();
		// 余额必须大于等于服务
		if (current.getUsableBalance().compareTo(serviceFee) >= 0) {
			// 进行转账
			SystemAccount systemAccount = systemAccountService.getCurrent();
			systemAccount.setUsableBalance(systemAccount.getUsableBalance().add(serviceFee));
			current.setUsableBalance(current.getUsableBalance().subtract(serviceFee));
			accountFlowService.serviceFeeFlow(serviceFee, current);
			systemAccountFlowService.serviceFeeFlow(systemAccount, serviceFee, current.getId());
			systemAccountService.save(systemAccount);
			accountService.save(current);
		} else {
			throw new LogicException(serviceFee.toString());
		}

		if (borrow.getRate().compareTo(minBorrowRate) >= 0 && borrow.getRate().compareTo(maxBorrowRate) <= 0) {
			// 处于发表前审核状态
			Integer releaseBeforeStatus = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
					SystemDictionaryUtil.RELEASE_BEFORE, systemDictionaryHashService);
			borrow.setBorrowStatus(releaseBeforeStatus);
			borrow.setBorrower(SecurityContextUtil.getCurrentUser());
			borrow.setBidNum(0);
			BigDecimal grossInterest = BigDecimalUtil.ZERO;

			if (monthInstalment.equals(repaymentMethod)) {
				grossInterest = BigDecimalUtil.getTotalInterest(borrowAmount, rate, repaymentMonths);
			} else if (monthExpire.equals(repaymentMethod)) {
				grossInterest = BigDecimalUtil.getTotalInterests(borrowAmount, rate, repaymentMonths);
			}
			borrow.setDeadline(DateUtils.addDays(DateUtil.getNewDate(), borrow.getBidDays()));
			borrow.setGrossInterest(grossInterest);
			borrow.setBidTotal(BigDecimalUtil.ZERO);
			borrowDao.save(borrow);
			UserInfo userInfo = userInfoService.getCurrent();
			// 设置借款状态
			userInfo.addStatus(UserInfoStatusUtil.OP_BORROW_PROCESS);
			userInfoService.save(userInfo);
		}
	}

	public PageResult pageQuery(BorrowQo qo) {
		Page<Borrow> page = borrowDao.findAll(
				Specifications.where(BorrowSpecification.equalBorrowStatus(qo.getBorrowStatus()))
						.and(BorrowSpecification.inBorrowStatusList(qo.getBorrowStatusList())),
				new PageRequest(qo.getPage(), qo.getSize(), Direction.DESC, StringUtil.APPLY_TIME,
						StringUtil.PUBLISH_TIME));
		List<SystemDictionaryItem> items = SystemDictionaryUtil.getItems(SystemDictionaryUtil.REPAYMENT_METHOD,
				systemDictionaryHashService);
		List<SystemDictionaryItem> borrowStatusList = SystemDictionaryUtil.getItems(SystemDictionaryUtil.BORROW_STATUS,
				systemDictionaryHashService);
		List<Borrow> content = page.getContent();
		for (Borrow borrow : content) {
			// 填充还款方式显示
			for (SystemDictionaryItem item : items) {
				if (Integer.valueOf(item.getItemValue()).equals(borrow.getRepaymentMethod())) {
					borrow.setRepaymentMethodDisplay(item.getItemName());
				}
			}
			for (SystemDictionaryItem item : borrowStatusList) {
				if (Integer.valueOf(item.getItemValue()).equals(borrow.getBorrowStatus())) {
					borrow.setBorrowStatusDisplay(item.getItemName());
				}
			}

		}
		return new PageResult(content, page.getTotalPages(), qo.getCurrentPage());
	}

	public Borrow get(Long id) {
		Borrow borrow = borrowDao.findOne(id);
		// 填充还款方式显示
		List<SystemDictionaryItem> items = SystemDictionaryUtil.getItems(SystemDictionaryUtil.REPAYMENT_METHOD,
				systemDictionaryHashService);
		items.forEach(item -> {
			if (Integer.valueOf(item.getItemValue()).equals(borrow.getRepaymentMethod())) {
				borrow.setRepaymentMethodDisplay(item.getItemName());
			}
		});
		return borrow;
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	public void publishAudit(Long id, Integer auditStatus, String remark) {
		// 得到当前用户借款对象
		Borrow borrow = get(id);
		if (borrow != null) {
			Integer auditPass = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
					SystemDictionaryUtil.AUDIT_PASS, systemDictionaryHashService);
			Integer pushAudit = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT_TYPE,
					SystemDictionaryUtil.PUSH_AUDIT, systemDictionaryHashService);
			// 创建一个审核历史对象 每一次审核对应一条历史记录
			createBorrowAuditHistroy(auditStatus, borrow, remark, pushAudit);
			Long borrowId = borrow.getBorrower().getId();
			if (auditPass.equals(auditStatus)) {
				Integer releaseIn = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
						SystemDictionaryUtil.RELEASE_IN, systemDictionaryHashService);
				// 修改状态进入招标中
				borrow.setBorrowStatus(releaseIn);
				// 修改发布时间
				borrow.setPublishTime(DateUtil.getNewDate());
				// 设置截止时间
				borrow.setDeadline(DateUtils.addDays(borrow.getPublishTime(), borrow.getBidDays()));
				// 修改可借金额
				Account account = accountService.get(borrowId);
				account.setRemainBorrowLimit(account.getRemainBorrowLimit().subtract(borrow.getBorrowAmount()));
				accountService.save(account);
			} else {
				Integer releaseReject = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
						SystemDictionaryUtil.RELEASE_REJECT, systemDictionaryHashService);
				// 发标前审核失败
				borrow.setBorrowStatus(releaseReject);
				// 拿到当前用户借款对象 删除发标状态
				UserInfo userInfo = userInfoService.get(borrowId);
				userInfo.deleteStatus(UserInfoStatusUtil.OP_BORROW_PROCESS);
				userInfoService.save(userInfo);
			}
			borrowDao.save(borrow);
		}
	}

	private void createBorrowAuditHistroy(Integer auditStatus, Borrow borrow, String remark, Integer auditType) {
		BorrowAuditHistroy histroy = new BorrowAuditHistroy();
		histroy.setSubmitter(borrow.getBorrower());
		histroy.setSubmissionTime(borrow.getApplyTime());
		histroy.setAuditor(SecurityContextUtil.getCurrentUser());
		histroy.setAuditTime(DateUtil.getNewDate());
		histroy.setBorrowId(borrow.getId());
		histroy.setRemark(remark);
		histroy.setAuditStatus(auditStatus);
		histroy.setAuditType(auditType);
		borrowAuditHistroyService.save(histroy);
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	public void save(Borrow borrow) {
		borrowDao.save(borrow);
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	public void fullAudit(Long id, String remark, Integer auditStatus) {
		Integer bidAudit = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
				SystemDictionaryUtil.BID_FULL, systemDictionaryHashService);
		// 得到借款对象 判定对象是否为满标状态
		Borrow borrow = get(id);
		if (borrow != null && borrow.getBorrowStatus().equals(bidAudit)) {
			Integer fullAuditType = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT_TYPE,
					SystemDictionaryUtil.PUSH_AUDIT, systemDictionaryHashService);
			// 创建一个借款审核历史对象
			createBorrowAuditHistroy(auditStatus, borrow, remark, fullAuditType);
			Integer auditPass = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
					SystemDictionaryUtil.AUDIT_PASS, systemDictionaryHashService);
			// 审核成功
			if (auditStatus.equals(auditPass)) {
				// 1.针对审核人
				// 1.1修改借款状态(还款状态)
				Integer paymentIn = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
						SystemDictionaryUtil.PAYMENT_IN, systemDictionaryHashService);
				borrow.setBorrowStatus(paymentIn);
				// 1.2生成还款计划和收款计划
				List<RepaymentPlan> repaymentPlans = createRepaymentPlan(borrow);

				// 2.针对借款人
				// 2.1借款人收到借款 借款人账户余额增加 生成借款流水
				Long borrowerId = borrow.getBorrower().getId();
				Account borrowAccount = accountService.get(borrowerId);
				borrowAccount.setUsableBalance(borrow.getBorrowAmount().add(borrowAccount.getUsableBalance()));
				accountFlowService.borrowPostFlow(borrow, borrowAccount);

				// 2.2借款人去掉借款状态
				UserInfo userInfo = userInfoService.get(borrowerId);
				userInfo.deleteStatus(UserInfoStatusUtil.OP_BORROW_PROCESS);
				userInfoService.save(userInfo);

				// 2.4借款人的待还总额增加
				BigDecimal returnAmount = borrow.getBorrowAmount().add(borrow.getGrossInterest());
				borrowAccount.setUnReturnAmount(borrowAccount.getUnReturnAmount().add(returnAmount));

				// 更新借款人账户
				accountService.save(borrowAccount);
				// 更新投资人账户
				updateInvestAccount(borrow, repaymentPlans);
			} else {
				// 审核失败
				Integer fullAuditReject = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
						SystemDictionaryUtil.FULL_AUDIT_REJECT, systemDictionaryHashService);
				cancelBid(borrow, fullAuditReject);
			}
			borrowDao.save(borrow);
		}
	}

	private void cancelBid(Borrow borrow, Integer borrowStatus) {
		// 修改借钱对象的状态为审核失败
		borrow.setBorrowStatus(borrowStatus);
		Long id = borrow.getBorrower().getId();
		// 更新用户没有借款在流程中
		UserInfo userInfo = userInfoService.get(id);
		userInfo.deleteStatus(UserInfoStatusUtil.OP_BORROW_PROCESS);
		userInfoService.save(userInfo);
		// 借款人恢复可借额度
		Account borrowAccount = accountService.get(id);
		borrowAccount.setRemainBorrowLimit(borrowAccount.getRemainBorrowLimit().add(borrow.getBorrowAmount()));
		accountService.save(borrowAccount);
		// 退钱
		List<Account> bidAccounts = new ArrayList<>();
		borrow.getBidList().forEach(bid -> {
			// 获取投标用户的账户
			Account bidAccount = accountService.get(bid.getInvestor().getId());
			// 余额增加 冻结金额减少
			bidAccount.setUsableBalance(bidAccount.getUsableBalance().add(bid.getBidAmount()));
			bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getBidAmount()));
			bidAccounts.add(bidAccount);
			// 生成一条取消投标金额流水
			accountFlowService.cancelBidFlow(bid, bidAccount);
		});
		// 统一更改投资人的账户
		accountService.save(bidAccounts);
	}

	private void updateInvestAccount(Borrow borrow, List<RepaymentPlan> repaymentPlans) {
		// 3.针对投资人 遍历所有投标
		Map<Long, Account> bidAccountMap = new HashMap<>(borrow.getBidList().size());
		// 3.1投资人冻结金额减少
		for (Bid bid : borrow.getBidList()) {
			Long bidUserId = bid.getInvestor().getId();
			// 获取到当前投资人的账户
			Account bidAccount = bidAccountMap.get(bidUserId);
			// 生成成功投标流水
			if (bidAccount == null) {
				bidAccount = accountService.get(bidUserId);
				bidAccountMap.put(bidUserId, bidAccount);
			}
			bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getBidAmount()));
			accountFlowService.bidSuccessFlow(bid, bidAccount);
		}
		// 3.2增加投资人总待收利息和待收本金
		for (RepaymentPlan repaymentPlan : repaymentPlans) {
			for (ReceiptPlan receiptPlan : repaymentPlan.getReceiptPlans()) {
				// 得到投资人账户
				Account account = bidAccountMap.get(receiptPlan.getReceiverId());
				// 增加投资人总待收利息和本金
				account.setUnReceiveInterest(account.getUnReceiveInterest().add(receiptPlan.getInterest()));
				account.setUnReceivePrincipal(account.getUnReceivePrincipal().add(receiptPlan.getPrincipal()));
			}
		}
		// 更新所有的投资人账户
		Collection<Account> values = bidAccountMap.values();
		List<Account> list = new ArrayList<>();
		values.forEach(account -> list.add(account));
		accountService.save(list);
	}

	private List<RepaymentPlan> createRepaymentPlan(Borrow borrow) {
		Date nowDate = DateUtil.getNewDate();
		List<RepaymentPlan> rsList = new ArrayList<>();

		Integer monthInstalment = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.REPAYMENT_METHOD,
				SystemDictionaryUtil.MONTH_INSTALMENT, systemDictionaryHashService);
		Integer monthExpire = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.REPAYMENT_METHOD,
				SystemDictionaryUtil.MONTH_EXPIRE, systemDictionaryHashService);
		List<PerMonth> interestMonth = new ArrayList<>();
		List<PerMonth> principalMonth = new ArrayList<>();
		BigDecimal borrowAmount = borrow.getBorrowAmount();
		BigDecimal rate = borrow.getRate();
		Integer repaymentMonths = borrow.getRepaymentMonths();
		if (monthInstalment.equals(borrow.getRepaymentMethod())) {
			interestMonth = BigDecimalUtil.getPerMonthInterest(borrowAmount, rate, repaymentMonths);
			principalMonth = BigDecimalUtil.getPerMonthPrincipal(borrowAmount, rate, repaymentMonths);
		} else if (monthExpire.equals(borrow.getRepaymentMethod())) {
			interestMonth = BigDecimalUtil.getPerMonthInterests(borrowAmount, rate, repaymentMonths);
			principalMonth = BigDecimalUtil.getPerMonthPrincipals(borrowAmount, rate, repaymentMonths);
		}

		Integer repaymentNormal = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.REPAYMENT_STATUS,
				SystemDictionaryUtil.REPAYMENT_NORMAL, systemDictionaryHashService);
		// 遍历还款期数
		for (int i = 1; i <= borrow.getRepaymentMonths(); i++) {
			// 针对每一期还款创建一个还款计划对象
			RepaymentPlan plan = new RepaymentPlan();
			plan.setBorrowId(borrow.getId());
			plan.setTitle(borrow.getTitle());
			plan.setBorrowerId(borrow.getBorrower().getId());
			plan.setMonthIndex(i);
			// 设置每一期的还款时间
			plan.setReturnTime(DateUtils.addMonths(nowDate, i));
			plan.setStatus(repaymentNormal);
			plan.setRepaymentMethod(borrow.getRepaymentMethod());
			for (PerMonth perMonth : interestMonth) {
				if (i == perMonth.getMonth()) {
					plan.setInterest(perMonth.getAmount());
				}
			}
			for (PerMonth perMonth : principalMonth) {
				if (i == perMonth.getMonth()) {
					plan.setPrincipal(perMonth.getAmount());
				}
			}
			plan.setTotalAmount(plan.getInterest().add(plan.getPrincipal()));
			repaymentPlanService.save(plan);
			rsList.add(plan);
			// 创建还款计划创建对应的收款计划
			createReceiptPlan(plan, borrow);
		}
		return rsList;
	}

	private void createReceiptPlan(RepaymentPlan repaymentPlan, Borrow borrow) {
		List<Bid> bidList = borrow.getBidList();
		List<ReceiptPlan> receiptPlans = new ArrayList<>();
		// 遍历每一个投标
		for (Integer i = 0; i < bidList.size(); i++) {
			Bid bid = bidList.get(i);
			// 为每一个标创建回款计划对象
			ReceiptPlan plan = new ReceiptPlan();

			plan.setBidAmount(bid.getBidAmount());
			plan.setBidId(bid.getId());
			plan.setBorrowId(borrow.getId());
			plan.setReturnUserId(repaymentPlan.getBorrowerId());
			plan.setReceiptTime(DateUtil.getNewDate());
			plan.setMonthIndex(repaymentPlan.getMonthIndex());
			plan.setRepaymentPlan(repaymentPlan);
			plan.setReceiverId(bid.getInvestor().getId());
			// 计算投标占总借款的比率
			BigDecimal rate = bid.getBidAmount().divide(borrow.getBidTotal(), BigDecimalUtil.CALC_SCALE,
					BigDecimal.ROUND_HALF_UP);
			plan.setPrincipal(repaymentPlan.getPrincipal().multiply(rate).setScale(BigDecimalUtil.SAVE_SCALE,
					BigDecimal.ROUND_HALF_UP));
			plan.setInterest(repaymentPlan.getInterest().multiply(rate).setScale(BigDecimalUtil.SAVE_SCALE,
					BigDecimal.ROUND_HALF_UP));
			plan.setTotalAmount(plan.getPrincipal().add(plan.getInterest()));

			receiptPlanService.save(plan);
			receiptPlans.add(plan);
		}
		repaymentPlan.setReceiptPlans(receiptPlans);
	}

}
