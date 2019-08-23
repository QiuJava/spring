package cn.loan.core.repository.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
		return new Specification<RepaymentPlan>() {
			@Override
			public Predicate toPredicate(Root<RepaymentPlan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (beginTime != null) {
					return cb.greaterThanOrEqualTo(root.get(StringUtil.RETURN_TIME), beginTime);
				}
				return null;
			}
		};
	}

	public static Specification<RepaymentPlan> lessThanOrEqualToReturnTime(Date endTime) {
		return new Specification<RepaymentPlan>() {
			@Override
			public Predicate toPredicate(Root<RepaymentPlan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (endTime != null) {
					return cb.lessThanOrEqualTo(root.get(StringUtil.RETURN_TIME), endTime);
				}
				return null;
			}
		};
	}

	public static Specification<RepaymentPlan> equalBorrowerId(Long borrowerId) {
		return new Specification<RepaymentPlan>() {
			@Override
			public Predicate toPredicate(Root<RepaymentPlan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (borrowerId != -1) {
					return cb.equal(root.get(StringUtil.BORROWER_ID), borrowerId);
				}
				return null;
			}
		};
	}
}
