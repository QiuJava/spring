package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.loan.core.entity.Withdraw;

/**
 * 提现数据操作
 * 
 * @author qiujian
 *
 */
public interface WithdrawDao extends JpaRepository<Withdraw, Long>, JpaSpecificationExecutor<Withdraw> {

}
