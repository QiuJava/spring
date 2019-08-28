package cn.loan.core.repository.specification;

import java.util.Date;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.Withdraw;
import cn.loan.core.util.StringUtil;

/**
 * 提现规格
 *
 * @author Qiujian
 *
 */
public class WithdrawSpecification {

	public static Specification<Withdraw> equalAuditStatus(Integer auditStatus) {
		return (root, query, cb) -> {
			if (auditStatus != null && auditStatus != -1) {
				return cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus);
			}
			return null;
		};
	}

	public static Specification<Withdraw> greaterThanOrEqualToSubmissionTime(Date beginTime) {
		return (root, query, cb) -> {
			if (beginTime != null) {
				Predicate beginTimePredicate = cb.greaterThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), beginTime);
				return beginTimePredicate;
			}
			return null;
		};
	}

	public static Specification<Withdraw> lessThanOrEqualToSubmissionTime(Date endTime) {
		return (root, query, cb) -> {
			if (endTime != null) {
				Predicate endTimePredicate = cb.lessThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), endTime);
				return endTimePredicate;
			}
			return null;
		};
	}

}
