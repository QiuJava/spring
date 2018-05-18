package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.business.SystemAccount;

public interface SystemAccountRepository extends JpaRepository<SystemAccount, Long>,JpaSpecificationExecutor<SystemAccount> {

}
