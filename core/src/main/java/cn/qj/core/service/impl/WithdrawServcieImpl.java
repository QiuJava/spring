package cn.qj.core.service.impl;

import java.math.BigDecimal;
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

import cn.qj.core.common.PageResult;
import cn.qj.core.consts.BidConst;
import cn.qj.core.entity.Account;
import cn.qj.core.entity.UserBankInfo;
import cn.qj.core.entity.UserInfo;
import cn.qj.core.entity.Withdraw;
import cn.qj.core.pojo.event.WithdrawEvent;
import cn.qj.core.pojo.qo.WithdrawQo;
import cn.qj.core.repository.WithdrawRepository;
import cn.qj.core.service.AccountFlowService;
import cn.qj.core.service.AccountService;
import cn.qj.core.service.UserBankInfoService;
import cn.qj.core.service.UserInfoService;
import cn.qj.core.service.WithdrawServcie;
import cn.qj.core.util.BidStateUtil;
import cn.qj.core.util.HttpServletContext;

/**
 * 提现服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class WithdrawServcieImpl implements WithdrawServcie {

	@Autowired
	private WithdrawRepository repository;

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserBankInfoService userBankInfoService;
	@Autowired
	private AccountFlowService accountFlowService;

	@Autowired
	private ApplicationContext ac;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void apply(BigDecimal moneyAmount) {
		Long id = HttpServletContext.getCurrentLoginInfo().getId();
		UserInfo userInfo = userInfoService.get(id);
		Account account = accountService.get(id);
		// 拿到当前银行账户对象
		UserBankInfo bankInfo = userBankInfoService.getByLoginInfoId(id);
		// 先判断当前用户是否在提现申请流程
		if (!userInfo.getIsWithdraw() && moneyAmount.compareTo(account.getUsableAmount()) <= 0
				&& moneyAmount.compareTo(BidConst.MIN_WITHDRAW_AMOUNT) >= 0) {
			// 创建一个提现用户对象，并设置相关属性
			Withdraw withdraw = new Withdraw();
			withdraw.setAccountNumber(bankInfo.getAccountNumber());
			withdraw.setBankForkName(bankInfo.getBankForkName());
			withdraw.setBankName(bankInfo.getBankName());
			withdraw.setRealName(bankInfo.getAccountName());
			withdraw.setMoneyAmount(moneyAmount);
			withdraw.setApplyTime(new Date());
			withdraw.setState(Withdraw.AUTH_NORMAL);
			withdraw.setApplier(HttpServletContext.getCurrentLoginInfo());
			// 保存对象
			repository.saveAndFlush(withdraw);
			// 修改用户状态
			account.setUsableAmount(account.getUsableAmount().subtract(moneyAmount));
			account.setFreezedAmount(account.getFreezedAmount().add(moneyAmount));
			// 生成提现流水
			accountFlowService.withdrawApplyFlow(withdraw, account);
			// 修改用户状态
			userInfo.addState(BidStateUtil.OP_WITHDRAW_PROCESS);
			accountService.update(account);
			userInfoService.update(userInfo);
		}
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void audit(Long id, String remark, Integer state) {
		// 拿到当前申请提现对象
		Withdraw withdraw = repository.findOne(id);
		// 判断当前是否处于审核状态
		if (withdraw.getState() == Withdraw.AUTH_NORMAL) {
			// 拿到当前申请人的账户对象
			Account currentAccount = accountService.get(withdraw.getApplier().getId());
			// 设置提相关状态
			withdraw.setAuditor((HttpServletContext.getCurrentLoginInfo()));
			withdraw.setRemark(remark);
			withdraw.setAuditTime(new Date());
			withdraw.setState(state);
			// 如果审核通过
			if (state.equals(Withdraw.AUTH_PASS)) {
				// 申请人冻结金额减少 生成成功提现流水
				currentAccount.setFreezedAmount(currentAccount.getFreezedAmount().subtract(withdraw.getMoneyAmount()));
				accountFlowService.withdrawSuccessFlow(withdraw, currentAccount);
			} else {// 审核失败 冻结金额减少 可用金额增多 生成提现审核失败流水
				currentAccount.setFreezedAmount(currentAccount.getFreezedAmount().subtract(withdraw.getMoneyAmount()));
				currentAccount.setUsableAmount(currentAccount.getUsableAmount().add(withdraw.getMoneyAmount()));
				accountFlowService.withdrawLoseFlow(withdraw, currentAccount);
			}
			// 去掉申请提现状态
			UserInfo userInfo = userInfoService.get(withdraw.getApplier().getId());
			userInfo.deleteState(BidStateUtil.OP_WITHDRAW_PROCESS);
			userInfoService.update(userInfo);
			accountService.update(currentAccount);
			repository.saveAndFlush(withdraw);
			// 提现成功发送短信
			ac.publishEvent(new WithdrawEvent(this, withdraw));
		}

	}

	@Override
	public PageResult list(WithdrawQo qo) {
		Page<Withdraw> page = repository.findAll(new Specification<Withdraw>() {
			@Override
			public Predicate toPredicate(Root<Withdraw> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (qo.getState() != -1) {
					list.add(cb.equal(root.get("state").as(Integer.class), qo.getState()));
				}
				if (qo.getBeginDate() != null) {
					list.add(cb.greaterThanOrEqualTo(root.get("applyTime").as(Date.class), qo.getBeginDate()));
				}
				if (qo.getEndDate() != null) {
					list.add(cb.lessThanOrEqualTo(root.get("applyTime").as(Date.class), qo.getEndDate()));
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize(), Direction.DESC, "applyTime"));
		if (page.getTotalElements() < 1) {
			return PageResult.empty(qo.getPageSize());
		}
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(), qo.getPageSize());
	}

}
