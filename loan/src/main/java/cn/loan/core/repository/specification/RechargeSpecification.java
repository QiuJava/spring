package cn.loan.core.repository.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.CompanyBankCard;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.Recharge;
import cn.loan.core.util.StringUtil;

/**
 * 充值规格
 * 
 * @author Qiujian
 *
 */
public class RechargeSpecification {

	public static Specification<Recharge> equalAuditStatus(Integer auditStatus) {
		return new Specification<Recharge>() {
			@Override
			public Predicate toPredicate(Root<Recharge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (auditStatus != null && auditStatus != -1) {
					return cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus);
				}
				return null;
			}
		};
	}

	public static Specification<Recharge> greaterThanOrEqualToSubmissionTime(Date beginTime) {
		return new Specification<Recharge>() {
			@Override
			public Predicate toPredicate(Root<Recharge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (beginTime != null) {
					cb.greaterThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), beginTime);
				}
				return null;
			}
		};
	}

	public static Specification<Recharge> lessThanOrEqualToSubmissionTime(Date endTime) {
		return new Specification<Recharge>() {
			@Override
			public Predicate toPredicate(Root<Recharge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (endTime != null) {
					cb.lessThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), endTime);
				}
				return null;
			}
		};
	}

	public static Specification<Recharge> equalSubmitter(LoginUser submitter) {
		return new Specification<Recharge>() {
			@Override
			public Predicate toPredicate(Root<Recharge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (submitter != null) {
					return cb.equal(root.get(StringUtil.SUBMITTER), submitter);
				}
				return null;
			}
		};
	}

	public static Specification<Recharge> likeSerialNumber(String serialNumber) {
		return new Specification<Recharge>() {
			@Override
			public Predicate toPredicate(Root<Recharge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (serialNumber != null) {
					return cb.like(root.get(StringUtil.SERIAL_NUMBER), serialNumber + StringUtil.PER_CENT);
				}
				return null;
			}
		};
	}

	public static Specification<Recharge> equalCompanyBankCard(CompanyBankCard bankCard) {
		return new Specification<Recharge>() {
			@Override
			public Predicate toPredicate(Root<Recharge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (bankCard != null && bankCard.getId() != -1) {
					return cb.equal(root.get(StringUtil.COMPANY_BANK_CARD), bankCard);
				}
				return null;
			}
		};
	}
}
