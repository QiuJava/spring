package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.business.SystemAccount;

/**
 * 系统账户相关持久化
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface SystemAccountRepository
		extends JpaRepository<SystemAccount, Long>, JpaSpecificationExecutor<SystemAccount> {

}
