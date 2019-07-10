package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.AccountFlow;

/**
 * 账户流水数据操作
 * 
 * @author qiujian
 *
 */
public interface AccountFlowDao extends JpaRepository<AccountFlow, Long> {

}
