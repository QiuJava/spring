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
import cn.loan.core.entity.BankCard;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.entity.Withdraw;
import cn.loan.core.entity.qo.WithdrawQo;
import cn.loan.core.repository.WithdrawDao;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;
import cn.loan.core.util.UserInfoStatusUtil;

/**
 * 提现服务
 * 
 * @author qiujian
 *
 */
@Service
public class WithdrawServcie {

	@Autowired
	private WithdrawDao withdrawDao;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private BankCardService bankCardService;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;
	@Autowired
	private AccountFlowService accountFlowService;

	@Transactional(rollbackFor = { RuntimeException.class })
	public void apply(BigDecimal moneyAmount) {
		LoginUser currentUser = SecurityContextUtil.getCurrentUser();
		Long id = currentUser.getId();
		UserInfo userInfo = userInfoService.get(id);
		Account account = accountService.get(id);
		// 拿到当前银行账户对象
		BankCard currentCard = bankCardService.getCurrent();
		BigDecimal minWithdrawAmount = SystemDictionaryUtil.getItemValueBigDecimal(SystemDictionaryUtil.INIT,
				SystemDictionaryUtil.MIN_WITHDRAW_AMOUNT, systemDictionaryHashService);

		// 先判断当前用户是否在提现申请流程
		if (!userInfo.isWithdrawProcess() && moneyAmount.compareTo(account.getUsableBalance()) <= 0
				&& moneyAmount.compareTo(minWithdrawAmount) >= 0) {
			// 创建一个提现用户对象，并设置相关属性
			Withdraw withdraw = new Withdraw();
			withdraw.setCardNumber(currentCard.getCardNumber());
			withdraw.setBankForkName(currentCard.getBankForkName());
			withdraw.setBankName(currentCard.getBankCode());

			withdraw.setRealName(userInfo.getRealName());
			withdraw.setAmount(moneyAmount);
			withdraw.setSubmissionTime(DateUtil.getNewDate());

			Integer auditNormal = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
					SystemDictionaryUtil.AUDIT_NORMAL, systemDictionaryHashService);
			withdraw.setAuditStatus(auditNormal);
			withdraw.setSubmitter(currentUser);
			// 保存对象
			withdrawDao.save(withdraw);

			// 增加冻结金额
			account.setUsableBalance(account.getUsableBalance().subtract(moneyAmount));
			account.setFreezedAmount(account.getFreezedAmount().add(moneyAmount));

			userInfo.addStatus(UserInfoStatusUtil.OP_WITHDRAW_PROCESS);
			accountFlowService.withdrawApplyFlow(withdraw, account);
			accountService.save(account);
			userInfoService.save(userInfo);
		}
	}

	@Transactional(rollbackFor = { RuntimeException.class })
	public void audit(Long id, String remark, Integer auditStatus) {
		// 拿到当前申请提现对象
		Withdraw withdraw = withdrawDao.findOne(id);
		Integer auditNormal = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
				SystemDictionaryUtil.AUDIT_NORMAL, systemDictionaryHashService);
		// 判断当前是否处于审核状态
		if (withdraw.getAuditStatus().equals(auditNormal)) {
			Long submitterId = withdraw.getSubmitter().getId();
			// 拿到当前申请人的账户对象
			Account currentAccount = accountService.get(submitterId);
			// 设置提相关状态
			withdraw.setAuditor(SecurityContextUtil.getCurrentUser());
			withdraw.setRemark(remark);
			withdraw.setAuditTime(DateUtil.getNewDate());
			withdraw.setAuditStatus(auditStatus);
			// 如果审核通过
			Integer auditPass = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
					SystemDictionaryUtil.AUDIT_PASS, systemDictionaryHashService);
			BigDecimal amount = withdraw.getAmount();
			if (auditStatus.equals(auditPass)) {
				// 扣除提现手续费 生成系统账户流水
				currentAccount.setFreezedAmount(currentAccount.getFreezedAmount().subtract(amount));
				// 申请人冻结金额减少 生成成功提现流水
				accountFlowService.withdrawSuccessFlow(withdraw, currentAccount);
			} else {
				// 审核失败 冻结金额减少 可用金额增多 生成提现审核失败流水
				currentAccount.setUsableBalance(currentAccount.getUsableBalance().add(amount));
				currentAccount.setFreezedAmount(currentAccount.getFreezedAmount().subtract(amount));
				accountFlowService.withdrawFailedFlow(withdraw, currentAccount);
			}
			// 去掉申请提现状态
			UserInfo userInfo = userInfoService.get(submitterId);
			userInfo.deleteStatus(UserInfoStatusUtil.OP_WITHDRAW_PROCESS);
			userInfoService.save(userInfo);
			accountService.save(currentAccount);
			withdrawDao.save(withdraw);
		}

	}

	public PageResult pageQuery(WithdrawQo qo) {
		Page<Withdraw> page = withdrawDao.findAll(new Specification<Withdraw>() {
			@Override
			public Predicate toPredicate(Root<Withdraw> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				Integer auditStatus = qo.getAuditStatus();
				if (auditStatus != null && auditStatus != -1) {
					list.add(cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus));
				}
				Date beginTime = qo.getBeginTime();
				if (beginTime != null) {
					Predicate beginTimePredicate = cb.greaterThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME),
							beginTime);
					list.add(beginTimePredicate);
				}
				Date endTime = qo.getEndTime();
				if (endTime != null) {
					Predicate endTimePredicate = cb.lessThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), endTime);
					list.add(endTimePredicate);
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getPage(), qo.getSize(), Direction.DESC, StringUtil.SUBMISSION_TIME));
		List<Withdraw> content = page.getContent();
		List<SystemDictionaryItem> audits = SystemDictionaryUtil.getItems(SystemDictionaryUtil.AUDIT,
				systemDictionaryHashService);
		for (Withdraw withdraw : content) {
			for (SystemDictionaryItem item : audits) {
				if (withdraw.getAuditStatus().equals(Integer.valueOf(item.getItemValue()))) {
					withdraw.setAuditStatusDisplay(item.getItemName());
				}
			}
		}
		return new PageResult(content, page.getTotalPages(), qo.getCurrentPage());
	}


}
