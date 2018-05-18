package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.domain.business.PaymentPlan;

public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {

}
