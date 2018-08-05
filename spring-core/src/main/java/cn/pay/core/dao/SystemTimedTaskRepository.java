package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.sys.SystemTimedTask;

public interface SystemTimedTaskRepository
		extends JpaRepository<SystemTimedTask, Long>, JpaSpecificationExecutor<SystemTimedTask> {
	
	SystemTimedTask findById(Long id);

}