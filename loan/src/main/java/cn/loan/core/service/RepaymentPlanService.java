package cn.loan.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.PageResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.Account;
import cn.loan.core.entity.Borrow;
import cn.loan.core.entity.ReceiptPlan;
import cn.loan.core.entity.RepaymentPlan;
import cn.loan.core.entity.qo.RepaymentPlanQo;
import cn.loan.core.repository.RepaymentPlanDao;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 还款计划服务
 * 
 * @author qiujian
 *
 */
@Service
public class RepaymentPlanService {

	@Autowired
	private RepaymentPlanDao repaymentPlanDao;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountFlowService accountFlowService;
	@Autowired
	private ReceiptPlanService receiptPlanService;
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	public void save(RepaymentPlan plan) {
		repaymentPlanDao.save(plan);
	}

	public PageResult pageQuery(RepaymentPlanQo qo) {
		Page<RepaymentPlan> page = repaymentPlanDao.findAll(new Specification<RepaymentPlan>() {
			@Override
			public Predicate toPredicate(Root<RepaymentPlan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				Date beginTime = qo.getBeginTime();
				if (beginTime != null) {
					list.add(cb.greaterThanOrEqualTo(root.get(StringUtil.RETURN_TIME), beginTime));
				}
				Date endTime = qo.getEndTime();
				if (endTime != null) {
					list.add(cb.lessThanOrEqualTo(root.get(StringUtil.RETURN_TIME), endTime));
				}
				Long borrowerId = qo.getBorrowerId();
				if (borrowerId != -1) {
					list.add(cb.equal(root.get(StringUtil.BORROWER_ID), borrowerId));
				}
				Predicate[] rs = new Predicate[list.size()];
				return cb.and(list.toArray(rs));
			}
		}, new PageRequest(qo.getPage(), qo.getSize(), Direction.ASC, StringUtil.RETURN_TIME));
		List<RepaymentPlan> content = page.getContent();
		return new PageResult(content, page.getTotalPages(), qo.getCurrentPage());
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void repayment(Long id) {
		// 得到还款计划
		RepaymentPlan plan = repaymentPlanDao.findOne(id);
		// 这一期还款的总金额
		BigDecimal returnTotalAmount = plan.getTotalAmount();
		// 得到借款人账户信息
		Account borrowAccount = accountService.getCurrent();
		// 借款人账户可用余额
		BigDecimal usableBalance = borrowAccount.getUsableBalance();
		// 1.判断当前用户是否处于还款中
		// 2.判断当前用户是否是还款用户
		// 3.判断用户的钱是否足够
		Integer repaymentNormal = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.REPAYMENT_STATUS,
				SystemDictionaryUtil.REPAYMENT_NORMAL, systemDictionaryHashService);
		if (plan.getStatus().equals(repaymentNormal) && plan.getBorrowerId().equals(borrowAccount.getId())
				&& usableBalance.compareTo(returnTotalAmount) >= 0) {
			// 1.还款计划改变状态
			// 修改成已还
			Integer repaymentPayback = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.REPAYMENT_STATUS,
					SystemDictionaryUtil.REPAYMENT_PAYBACK, systemDictionaryHashService);
			plan.setStatus(repaymentPayback);
			// 针对还款人
			// 2.可用余额减少，生成还款成功流水
			borrowAccount.setUsableBalance(usableBalance.subtract(returnTotalAmount));
			accountFlowService.repaymentFlow(plan, borrowAccount);

			// 设置还款时间
			plan.setActualReturnTime(DateUtil.getNewDate());
			// 3.待还减少
			borrowAccount.setUnReturnAmount(borrowAccount.getUnReturnAmount().subtract(returnTotalAmount));
			// 4.剩余信用额度增加
			borrowAccount.setRemainBorrowLimit(borrowAccount.getRemainBorrowLimit().add(plan.getPrincipal()));
			// 针对投资人
			// 5.遍历回款明细
			for (ReceiptPlan receiptPlan : plan.getReceiptPlans()) {
				// 拿到投资人账户
				Account bidAccount = accountService.get(receiptPlan.getReceiverId());
				// 6.账户余额增加，生成成功收款明细
				if (bidAccount != null) {
					receiptPlan.setActualReceiptTime(DateUtil.getNewDate());
					accountFlowService.receiptFlow(receiptPlan, bidAccount);
					// 7.待收利息和待收本金减少
					bidAccount.setUnReceivePrincipal(
							bidAccount.getUnReceivePrincipal().subtract(receiptPlan.getPrincipal()));
					bidAccount.setUnReceiveInterest(
							bidAccount.getUnReceiveInterest().subtract(receiptPlan.getInterest()));
					bidAccount.setUsableBalance(bidAccount.getUsableBalance().add(receiptPlan.getPrincipal())
							.add(receiptPlan.getInterest()));
					accountService.save(bidAccount);
					receiptPlanService.save(receiptPlan);
				}
			}
			// 8.如果当前还款是最后一期 改变借款人状态
			// 得到借款对象
			Borrow borrow = borrowService.get(plan.getBorrowId());
			if (plan.getMonthIndex().equals(borrow.getRepaymentMonths())) {
				// 已还清状态
				borrow.setBorrowStatus(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
						SystemDictionaryUtil.PAY_OFF, systemDictionaryHashService));
				borrowService.save(borrow);
			}
			repaymentPlanDao.save(plan);
			accountService.save(borrowAccount);
		}
	}

}
