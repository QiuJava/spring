package cn.qj.core.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.common.LogicException;
import cn.qj.core.consts.BidConst;
import cn.qj.core.entity.Account;
import cn.qj.core.entity.Borrow;
import cn.qj.core.entity.PaymentPlan;
import cn.qj.core.entity.RepaymentSchedule;
import cn.qj.core.pojo.event.PaymentPlanEvent;
import cn.qj.core.pojo.qo.RepaymentScheduleQo;
import cn.qj.core.pojo.vo.PageResult;
import cn.qj.core.repository.RepaymentScheduleRepository;
import cn.qj.core.service.AccountFlowService;
import cn.qj.core.service.AccountService;
import cn.qj.core.service.BorrowService;
import cn.qj.core.service.PaymentPlanService;
import cn.qj.core.service.RepaymentScheduleService;
import cn.qj.core.service.SendSmsService;
import cn.qj.core.util.DateUtil;
import cn.qj.core.util.HttpServletContext;

/**
 * 收款计划服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class RepaymentScheduleServcieImpl implements RepaymentScheduleService {

	@Autowired
	private RepaymentScheduleRepository repository;

	@Autowired
	private AccountService accountService;
	@Autowired
	private PaymentPlanService paymentPlanService;
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private SendSmsService sendSmsService;

	@Autowired
	private AccountFlowService accountFlowService;

	@Autowired
	private ApplicationContext ac;

	@Override
	public PageResult list(RepaymentScheduleQo qo) {
		Page<RepaymentSchedule> page = repository.findAll(new Specification<RepaymentSchedule>() {
			@Override
			public Predicate toPredicate(Root<RepaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (qo.getState() != -1) {
					list.add(cb.equal(root.get("state").as(Integer.class), qo.getState()));
				}
				if (qo.getBeginDate() != null) {
					list.add(cb.greaterThanOrEqualTo(root.get("deadline").as(Date.class), qo.getBeginDate()));
				}
				if (qo.getEndDate() != null) {
					list.add(cb.lessThanOrEqualTo(root.get("deadline").as(Date.class), qo.getEndDate()));
				}
				if (qo.getUserId() != -1) {
					list.add(cb.equal(root.get("borrowUserId"), qo.getUserId()));
				}
				Predicate[] rs = new Predicate[list.size()];
				return cb.and(list.toArray(rs));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize(), Direction.ASC, "deadline"));
		if (page.getTotalElements() < 1) {
			return PageResult.empty(qo.getPageSize());
		}
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(), qo.getPageSize());
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void repay(Long id) {
		// 得到还款计划
		RepaymentSchedule rs = repository.findOne(id);
		// 这一期还款的总金额
		BigDecimal returnTotalAmount = rs.getTotalAmount();
		// 得到借款人账户信息
		Account currentAccount = accountService.get(HttpServletContext.getCurrentLoginInfo().getId());
		// 借款人账户可用余额
		BigDecimal usableAmount = currentAccount.getUsableAmount();
		// 1.判断当前用户是否处于还款中
		// 2.判断当前用户是否是还款用户
		// 3.判断用户的钱是否足够
		if (rs != null && rs.getState().equals(RepaymentSchedule.NORMAL)
				&& rs.getBorrowUserId().equals(currentAccount.getId())
				&& usableAmount.compareTo(returnTotalAmount) >= 0) {
			// 1.还款计划改变状态
			// 修改成已还
			rs.setState(RepaymentSchedule.PAYBACK);
			// 针对还款人
			// 2.可用余额减少，生成还款成功流水
			currentAccount.setUsableAmount(usableAmount.subtract(returnTotalAmount));
			accountFlowService.repayFlow(rs, currentAccount);

			// 设置还款时间
			rs.setPayDate(new Date());
			// 3.待还减少
			currentAccount.setUnReturnAmount(currentAccount.getUnReturnAmount().subtract(returnTotalAmount));
			// 4.剩余信用额度增加
			currentAccount.setRemainBorrowLimit(currentAccount.getRemainBorrowLimit().add(returnTotalAmount));
			// 针对投资人
			// 5.遍历回款明细
			for (PaymentPlan pp : rs.getPaymentPlanList()) {
				// 拿到投资人账户
				Account bidAccount = accountService.get(pp.getCollectLoginInfoId());
				// 6.账户余额增加，生成成功收款明细
				if (bidAccount != null) {
					pp.setPayDate(new Date());

					accountFlowService.receiptFlow(pp, bidAccount);
					// 7.待收利息和待收本金减少
					bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().subtract(pp.getPrincipal()));
					bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().subtract(pp.getInterest()));
					bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(pp.getPrincipal()));
					bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(pp.getInterest()));
					accountService.update(bidAccount);
					paymentPlanService.saveAndUpdate(pp);

					// 收款成功发送短信
					ac.publishEvent(new PaymentPlanEvent(this, pp));
				}
			}
			// 8.如果当前还款是最后一期 改变借款人状态
			// 得到借款对象
			Borrow borrow = borrowService.get(rs.getBorrowId());
			if (rs.getMonthIndex().equals(borrow.getMonthReturn())) {
				// 已还清状态
				borrow.setState(BidConst.BORROW_STATE_COMPLETE_PAY_BACK);
				borrowService.update(borrow);
			}
			repository.saveAndFlush(rs);
			accountService.update(currentAccount);
		}
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void saveAndUpdate(RepaymentSchedule rs) {
		repository.saveAndFlush(rs);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void autoRepay() {
		// 2.判断账户余额是否满足还款
		for (RepaymentSchedule repaymentSchedule : repayList()) {
			Account borrowAccount = accountService.get(repaymentSchedule.getBorrowUserId());
			if (borrowAccount.getUsableAmount().compareTo(repaymentSchedule.getTotalAmount()) >= 0) {
				// 执行还款
				repay(repaymentSchedule.getId());
			}
		}
	}

	private List<RepaymentSchedule> repayList() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dateStr = format.format(date);
		String endOfDayStr = format.format(DateUtil.endOfDay(date));
		// 1.查询出所有到还款时间 并且状态是正常待还
		return repository.findAll(new Specification<RepaymentSchedule>() {
			@Override
			public Predicate toPredicate(Root<RepaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("state").as(Integer.class), RepaymentSchedule.NORMAL));
				try {
					list.add(cb.between(root.get("deadline").as(Date.class), format.parse(dateStr),
							format.parse(endOfDayStr)));
				} catch (ParseException e) {
					throw new LogicException(e.getMessage());
				}
				Predicate[] rs = new Predicate[list.size()];
				return cb.and(list.toArray(rs));
			}
		});
	}

	@Override
	public void autoRepaySms() {
		for (RepaymentSchedule repaymentSchedule : repayList()) {
			sendSmsService.repayWarn(repaymentSchedule);
		}
	}

}
