package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.PaymentPlan;

/**
 * 回款计划持久化相关
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {

}
