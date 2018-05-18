package cn.pay.core.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.business.BorrowAuditHistroy;

public interface BorrowAuditHistroyRepository extends JpaRepository<BorrowAuditHistroy, Long> {
	
	/**
	 * 查询这个借款的审核历史
	 * @param id
	 * @return
	 */
	List<BorrowAuditHistroy> findByBorrowId(Long id);

}
