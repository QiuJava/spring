package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.business.RepaymentSchedule;

public interface RepaymentScheduleRepository
		extends JpaRepository<RepaymentSchedule, Long>, JpaSpecificationExecutor<RepaymentSchedule> {

}
