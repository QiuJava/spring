package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.Account;

/**
 * 账户数据操作
 * 
 * @author qiujian
 *
 */
public interface AccountDao extends JpaRepository<Account, Long> {

}
