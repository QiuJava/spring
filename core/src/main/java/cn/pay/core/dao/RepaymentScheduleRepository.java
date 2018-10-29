package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.entity.business.RepaymentSchedule;

/**
 * 收款计划
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface RepaymentScheduleRepository
		extends JpaRepository<RepaymentSchedule, Long>, JpaSpecificationExecutor<RepaymentSchedule> {

}
