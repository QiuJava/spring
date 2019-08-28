package cn.loan.core.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.Borrow;
import cn.loan.core.util.StringUtil;

public class BorrowSpecification {

	public static Specification<Borrow> equalBorrowStatus(Integer borrowStatus) {
		return new Specification<Borrow>() {
			@Override
			public Predicate toPredicate(Root<Borrow> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (borrowStatus != null && borrowStatus != -1) {
					return cb.equal(root.get(StringUtil.BORROW_STATUS), borrowStatus);
				}
				return null;
			}
		};
	}

	public static Specification<Borrow> inBorrowStatusList(Integer[] borrowStatusList) {
		return new Specification<Borrow>() {
			@Override
			public Predicate toPredicate(Root<Borrow> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (borrowStatusList != null && borrowStatusList.length > 0) {
					In<Integer> in = cb.in(root.get(StringUtil.BORROW_STATUS));
					for (Integer status : borrowStatusList) {
						in.value(status);
					}
					return in;
				}
				return null;
			}
		};
	}

}
