package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.business.Recharge;

public interface RechargeRepository extends JpaRepository<Recharge, Long>, JpaSpecificationExecutor<Recharge> {

}
