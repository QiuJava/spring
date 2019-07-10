package cn.loan.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.BorrowAuditHistroy;

/**
 * 审核历史数据操作
 * 
 * @author qiujian
 *
 */
public interface BorrowAuditHistroyDao extends JpaRepository<BorrowAuditHistroy, Long> {

	/**
	 * 查询借款的审核历史
	 * 
	 * @param id
	 * @return
	 */
	List<BorrowAuditHistroy> findByBorrowId(Long id);

}
