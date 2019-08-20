package cn.loan.core.repository.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.LoginLog;
import cn.loan.core.util.StringUtil;

public class LoginLogSpecification {

	public static Specification<LoginLog> equalLoginStatus(Integer loginStatus) {
		return new Specification<LoginLog>() {
			public Predicate toPredicate(Root<LoginLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (loginStatus != null && loginStatus != -1) {
					return cb.equal(root.get(StringUtil.LOGIN_STATUS), loginStatus);
				}
				return null;
			}
		};
	}

	public static Specification<LoginLog> greaterThanOrEqualToLoginTime(Date beginTime) {
		return new Specification<LoginLog>() {
			public Predicate toPredicate(Root<LoginLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (beginTime != null) {
					return cb.greaterThanOrEqualTo(root.get(StringUtil.LOGIN_TIME), beginTime);
				}
				return null;
			}
		};
	}

	public static Specification<LoginLog> lessThanOrEqualToLoginTime(Date endTime) {
		return new Specification<LoginLog>() {
			public Predicate toPredicate(Root<LoginLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (endTime != null) {
					return cb.lessThanOrEqualTo(root.get(StringUtil.LOGIN_TIME), endTime);
				}
				return null;
			}
		};
	}

	public static Specification<LoginLog> likeUsername(String username) {
		return new Specification<LoginLog>() {
			public Predicate toPredicate(Root<LoginLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (StringUtil.hasLength(username)) {
					return cb.like(root.get(StringUtil.USERNAME), username + StringUtil.PER_CENT);
				}
				return null;
			}
		};
	}
}
