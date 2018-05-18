package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.business.Withdraw;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long>, JpaSpecificationExecutor<Withdraw> {

}
