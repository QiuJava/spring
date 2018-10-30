package cn.qj.core.service;

import cn.qj.core.entity.SystemTimedTask;
import cn.qj.core.pojo.qo.SystemTimedTaskQo;
import cn.qj.core.pojo.vo.PageResult;

/**
 * 系统定时任务服务
 * 
 * @author Qiujian
 *
 */
public interface SystemTimedTaskService {

	/**
	 * 保存系统定时任务
	 * 
	 * @param systemTimedTask
	 * @return
	 */
	SystemTimedTask saveSystemTimedTask(SystemTimedTask systemTimedTask);

	/**
	 * 更新系统定时任务
	 * 
	 * @param systemTimedTask
	 * @return
	 */
	SystemTimedTask updateSystemTimedTask(SystemTimedTask systemTimedTask);

	/**
	 * 定时任务分页查询
	 * 
	 * @param qo
	 * @return
	 */
	PageResult pageQuerySystemTimedTask(SystemTimedTaskQo qo);

	/**
	 * 根据id查询定时任务信息
	 * 
	 * @param id
	 * @return
	 */
	SystemTimedTask getSystemTimedTaskById(Long id);

	/**
	 * 根据定时任务Id删除
	 * 
	 * @param id
	 */
	void deleteById(Long id);

}
