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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.common.LogicException;
import cn.qj.core.consts.BidConst;
import cn.qj.core.entity.Borrow;
import cn.qj.core.entity.SystemAccount;
import cn.qj.core.entity.SystemAccountFlow;
import cn.qj.core.repository.SystemAccountRepository;
import cn.qj.core.service.SystemAccountFlowService;
import cn.qj.core.service.SystemAccountService;

/**
 * 系统账户服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
@Transactional(rollbackFor = { RuntimeException.class })
public class SystemAccountServiceImpl implements SystemAccountService {

	@Autowired
	private SystemAccountRepository repository;
	@Autowired
	private SystemAccountFlowService systemAccountFlowService;

	@Override
	public void chargeManager(Borrow borrow, BigDecimal serviceCharge) {
		List<SystemAccount> list = repository.findAll(new Specification<SystemAccount>() {
			@Override
			public Predicate toPredicate(Root<SystemAccount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				Date date = new Date();
				list.add(cb.or(cb.greaterThanOrEqualTo(root.get("endDate"), date),
						cb.lessThanOrEqualTo(root.get("beginDate"), date)));
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		});
		// 得到当前系统账户
		SystemAccount systemAccount = list.get(0);
		// 增加系统账户金额
		systemAccount.setTotalBalance(systemAccount.getTotalBalance().add(serviceCharge));
		// 生成一条系统账户流水
		SystemAccountFlow flow = new SystemAccountFlow();
		flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_CHARGE);
		flow.setAmount(serviceCharge);
		flow.setBalance(systemAccount.getTotalBalance());
		flow.setCreateDate(new Date());
		flow.setFreezedAmount(systemAccount.getFreezedAmount());
		flow.setNote("借款" + borrow.getTitle() + "成功缴纳平台手续费");
		flow.setSystemAccountId(systemAccount.getId());
		flow.setTargetUserId(borrow.getCreateUser().getId());
		update(systemAccount);
		systemAccountFlowService.saveAndUpdate(flow);
	}

	@Override
	public void update(SystemAccount systemAccount) {
		SystemAccount accountUpdate = repository.saveAndFlush(systemAccount);
		if (accountUpdate == null) {
			throw new LogicException("系统账户信息更新乐观锁异常");
		}
	}

	@Override
	public Long count() {
		return repository.count();
	}

	@Override
	public void save(SystemAccount systemAccount) {
		repository.saveAndFlush(systemAccount);
	}

}
