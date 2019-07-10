package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.SystemAccount;

/**
 * 系统账户数据操作
 * 
 * @author qiujian
 *
 */
public interface SystemAccountDao extends JpaRepository<SystemAccount, Long> {

}
