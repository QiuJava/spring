package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.ReceiptPlan;

/**
 * 收款计划数据操作
 * 
 * @author qiujian
 *
 */
public interface ReceiptPlanDao extends JpaRepository<ReceiptPlan, Long> {

}
