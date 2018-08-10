package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.sys.SystemTimedTask;

/**
 * 系统定时任务持久化
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface SystemTimedTaskRepository
		extends JpaRepository<SystemTimedTask, Long>, JpaSpecificationExecutor<SystemTimedTask> {

	/**
	 * 根据id查询单个对象
	 * 
	 * @param id
	 * @return
	 */
	SystemTimedTask findById(Long id);

}