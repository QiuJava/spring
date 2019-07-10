package cn.loan.core.service;

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
import cn.loan.core.entity.CompanyBankCard;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.Recharge;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.qo.RechargeQo;
import cn.loan.core.repository.RechargeDao;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 充值服务
 * 
 * @author qiujian
 *
 */
@Service
public class RechargeService {

	@Autowired
	private RechargeDao rechargeDao;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountFlowService accountFlowService;

	@Transactional(rollbackFor = RuntimeException.class)
	public void apply(Recharge recharge) {
		recharge.setSubmissionTime(DateUtil.getNewDate());
		recharge.setSubmitter(SecurityContextUtil.getCurrentUser());
		Integer auditNormal = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
				SystemDictionaryUtil.AUDIT_NORMAL, systemDictionaryHashService);
		recharge.setAuditStatus(auditNormal);
		rechargeDao.save(recharge);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void audit(Long id, String remark, Integer auditStatus) {
		Recharge recharge = rechargeDao.findOne(id);
		recharge.setAuditor(SecurityContextUtil.getCurrentUser());
		recharge.setAuditTime(DateUtil.getNewDate());
		recharge.setAuditStatus(auditStatus);
		recharge.setRemark(remark);
		Integer auditPass = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
				SystemDictionaryUtil.AUDIT_PASS, systemDictionaryHashService);
		if (auditStatus.equals(auditPass)) {
			Account account = accountService.get(recharge.getSubmitter().getId());
			// 修改账户可用余额
			account.setUsableBalance(account.getUsableBalance().add(recharge.getAmount()));
			// 创建充值流水
			accountFlowService.rechargeFolw(recharge, account);
			accountService.save(account);
		}
		rechargeDao.save(recharge);

	}

	public PageResult pageQuery(RechargeQo qo) {
		Page<Recharge> page = rechargeDao.findAll(new Specification<Recharge>() {
			@Override
			public Predicate toPredicate(Root<Recharge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				Integer auditStatus = qo.getAuditStatus();
				if (auditStatus != null && auditStatus != -1) {
					list.add(cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus));
				}
				Date beginTime = qo.getBeginTime();
				if (beginTime != null) {
					list.add(cb.greaterThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), beginTime));
				}
				Date endTime = qo.getEndTime();
				if (endTime != null) {
					list.add(cb.lessThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), endTime));
				}
				LoginUser submitter = qo.getSubmitter();
				if (submitter != null) {
					list.add(cb.equal(root.get(StringUtil.SUBMITTER), submitter));
				}
				String serialNumber = qo.getSerialNumber();
				if (serialNumber != null) {
					list.add(cb.like(root.get(StringUtil.SERIAL_NUMBER), serialNumber + StringUtil.PER_CENT));
				}
				CompanyBankCard bankCard = qo.getCompanyBankCard();
				if (bankCard != null && bankCard.getId() != -1) {
					list.add(cb.equal(root.get(StringUtil.COMPANY_BANK_CARD), bankCard));
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getPage(), qo.getSize(), Direction.DESC, StringUtil.SUBMISSION_TIME));
		List<Recharge> content = page.getContent();
		List<SystemDictionaryItem> audits = SystemDictionaryUtil.getItems(SystemDictionaryUtil.AUDIT,
				systemDictionaryHashService);
		for (Recharge recharge : content) {
			for (SystemDictionaryItem item : audits) {
				if (recharge.getAuditStatus().equals(Integer.valueOf(item.getItemValue()))) {
					recharge.setAuditStatusDisplay(item.getItemName());
				}
			}
		}
		return new PageResult(content, page.getTotalPages(), qo.getCurrentPage());
	}

}
