package cn.loan.core.repository.specification;

import java.util.Date;

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
		return (root, query, cb) -> {
			if (auditStatus != null && auditStatus != -1) {
				return cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus);
			}
			return null;
		};
	}

	public static Specification<RealAuth> greaterThanOrEqualToSubmissionTime(Date beginTime) {
		return (root, query, cb) -> {
			if (beginTime != null) {
				cb.greaterThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), beginTime);
			}
			return null;
		};
	}

	public static Specification<RealAuth> lessThanOrEqualToSubmissionTime(Date endTime) {
		return (root, query, cb) -> {
			if (endTime != null) {
				cb.lessThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), endTime);
			}
			return null;
		};
	}

}
