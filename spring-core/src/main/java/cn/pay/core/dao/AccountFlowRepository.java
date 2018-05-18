package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.business.AccountFlow;

public interface AccountFlowRepository extends JpaRepository<AccountFlow, Long> {

}
