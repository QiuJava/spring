package cn.loan.core.repository.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.RealAuth;
import cn.loan.core.util.StringUtil;

/**
 * 实名认证规约
 * 
 * @author Qiujian
 *
 */
public class RealAuthSpecification {

	public static Specification<RealAuth> equalAuditStatus(Integer auditStatus) {
		return new Specification<RealAuth>() {
			@Override
			public Predicate toPredicate(Root<RealAuth> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (auditStatus != null && auditStatus != -1) {
					return cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus);
				}
				return null;
			}
		};
	}

	public static Specification<RealAuth> greaterThanOrEqualToSubmissionTime(Date beginTime) {
		return new Specification<RealAuth>() {
			@Override
			public Predicate toPredicate(Root<RealAuth> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (beginTime != null) {
					cb.greaterThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), beginTime);
				}
				return null;
			}
		};
	}

	public static Specification<RealAuth> lessThanOrEqualToSubmissionTime(Date endTime) {
		return new Specification<RealAuth>() {
			@Override
			public Predicate toPredicate(Root<RealAuth> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (endTime != null) {
					cb.lessThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), endTime);
				}
				return null;
			}
		};
	}

}
