package cn.loan.core.repository.specification;

import java.util.Date;

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
		return (root, query, cb) -> {
			if (auditStatus != null && auditStatus != -1) {
				return cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus);
			}
			return null;
		};
	}

	public static Specification<Recharge> greaterThanOrEqualToSubmissionTime(Date beginTime) {
		return (root, query, cb) -> {
			if (beginTime != null) {
				cb.greaterThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), beginTime);
			}
			return null;
		};
	}

	public static Specification<Recharge> lessThanOrEqualToSubmissionTime(Date endTime) {
		return (root, query, cb) -> {
			if (endTime != null) {
				cb.lessThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), endTime);
			}
			return null;
		};
	}

	public static Specification<Recharge> equalSubmitter(LoginUser submitter) {
		return (root, query, cb) -> {
			if (submitter != null) {
				return cb.equal(root.get(StringUtil.SUBMITTER), submitter);
			}
			return null;
		};
	}

	public static Specification<Recharge> likeSerialNumber(String serialNumber) {
		return (root, query, cb) -> {
			if (serialNumber != null) {
				return cb.like(root.get(StringUtil.SERIAL_NUMBER), serialNumber.concat(StringUtil.PER_CENT));
			}
			return null;
		};
	}

	public static Specification<Recharge> equalCompanyBankCard(CompanyBankCard bankCard) {
		return (root, query, cb) -> {
			if (bankCard != null && bankCard.getId() != -1) {
				return cb.equal(root.get(StringUtil.COMPANY_BANK_CARD), bankCard);
			}
			return null;
		};
	}
}
