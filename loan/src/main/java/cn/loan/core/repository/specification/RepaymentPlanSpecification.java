package cn.loan.core.repository.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.RepaymentPlan;
import cn.loan.core.util.StringUtil;

/**
 * 还款计划规格
 * 
 * @author Qiujian
 *
 */
public class RepaymentPlanSpecification {

	public static Specification<RepaymentPlan> greaterThanOrEqualToReturnTime(Date beginTime) {
		return (root, query, cb) -> {
			if (beginTime != null) {
				return cb.greaterThanOrEqualTo(root.get(StringUtil.RETURN_TIME), beginTime);
			}
			return null;
		};
	}

	public static Specification<RepaymentPlan> lessThanOrEqualToReturnTime(Date endTime) {
		return (root, query, cb) -> {
			if (endTime != null) {
				return cb.lessThanOrEqualTo(root.get(StringUtil.RETURN_TIME), endTime);
			}
			return null;
		};
	}

	public static Specification<RepaymentPlan> equalBorrowerId(Long borrowerId) {
		return (root, query, cb) -> {
			if (borrowerId != -1) {
				return cb.equal(root.get(StringUtil.BORROWER_ID), borrowerId);
			}
			return null;
		};
	}
}
