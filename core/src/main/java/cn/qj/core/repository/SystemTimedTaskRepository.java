package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.qj.core.entity.SystemTimedTask;

/**
 * 系统定时任务持久化
 * 
 * @author Qiujian
 * @date 2018/8/10
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