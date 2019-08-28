package cn.loan.core.repository.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.LoginLog;
import cn.loan.core.util.StringUtil;

/**
 * 登录日志规格
 * 
 * @author Qiujian
 *
 */
public class LoginLogSpecification {

	public static Specification<LoginLog> equalLoginStatus(Integer loginStatus) {
		return (root, query, cb) -> {
			if (loginStatus != null && loginStatus != -1) {
				return cb.equal(root.get(StringUtil.LOGIN_STATUS), loginStatus);
			}
			return null;
		};
	}

	public static Specification<LoginLog> greaterThanOrEqualToLoginTime(Date beginTime) {
		return (root, query, cb) -> {
			if (beginTime != null) {
				return cb.greaterThanOrEqualTo(root.get(StringUtil.LOGIN_TIME), beginTime);
			}
			return null;
		};
	}

	public static Specification<LoginLog> lessThanOrEqualToLoginTime(Date endTime) {
		return (root, query, cb) -> {
			if (endTime != null) {
				return cb.lessThanOrEqualTo(root.get(StringUtil.LOGIN_TIME), endTime);
			}
			return null;
		};

	}

	public static Specification<LoginLog> likeUsername(String username) {
		return (root, query, cb) -> {
			if (StringUtil.hasLength(username)) {
				return cb.like(root.get(StringUtil.USERNAME), username.concat(StringUtil.PER_CENT));
			}
			return null;
		};
	}
}
