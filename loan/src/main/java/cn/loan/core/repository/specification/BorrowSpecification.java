package cn.loan.core.repository.specification;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.Borrow;
import cn.loan.core.util.StringUtil;

public class BorrowSpecification {

	public static Specification<Borrow> equalBorrowStatus(Integer borrowStatus) {
		return (root, query, cb) -> {
			if (borrowStatus != null && borrowStatus != -1) {
				return cb.equal(root.get(StringUtil.BORROW_STATUS), borrowStatus);
			}
			return null;
		};
	}

	public static Specification<Borrow> inBorrowStatusList(Integer[] borrowStatusList) {
		return (root, query, cb) -> {
			if (borrowStatusList != null && borrowStatusList.length > 0) {
				In<Integer> in = cb.in(root.get(StringUtil.BORROW_STATUS));
				List<Integer> asList = Arrays.asList(borrowStatusList);
				asList.stream().forEach(status -> in.value(status));
				return in;
			}
			return null;
		};
	}

}
