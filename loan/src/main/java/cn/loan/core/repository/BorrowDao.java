package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.loan.core.entity.Borrow;

/**
 * 借款数据操作
 * 
 * @author qiujian
 *
 */
public interface BorrowDao extends JpaRepository<Borrow, Long>, JpaSpecificationExecutor<Borrow> {

}
