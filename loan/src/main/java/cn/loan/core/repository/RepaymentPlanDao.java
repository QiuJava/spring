package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.loan.core.entity.RepaymentPlan;

/**
 * 还款计划数据操作
 * 
 * @author qiujian
 *
 */
public interface RepaymentPlanDao extends JpaRepository<RepaymentPlan, Long>, JpaSpecificationExecutor<RepaymentPlan> {

}
