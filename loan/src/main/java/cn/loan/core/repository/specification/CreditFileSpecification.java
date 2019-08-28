package cn.loan.core.repository.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.CreditFile;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.util.StringUtil;

/**
 * 信用材料规格
 * 
 * @author Qiujian
 *
 */
public class CreditFileSpecification {
	public static Specification<CreditFile> equalAuditStatus(Integer auditStatus) {
		return (root, query, cb) -> {
			if (auditStatus != null && auditStatus != -1) {
				return cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus);
			}
			return null;
		};
	}

	public static Specification<CreditFile> greaterThanOrEqualToSubmissionTime(Date beginTime) {
		return (root, query, cb) -> {
			if (beginTime != null) {
				cb.greaterThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), beginTime);
			}
			return null;
		};
	}

	public static Specification<CreditFile> lessThanOrEqualToSubmissionTime(Date endTime) {
		return (root, query, cb) -> {
			if (endTime != null) {
				cb.lessThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), endTime);
			}
			return null;
		};
	}

	public static Specification<CreditFile> equalSubmitter(LoginUser submitter) {
		return (root, query, cb) -> {
			return cb.equal(root.get(StringUtil.SUBMITTER), submitter);
		};
	}

}
