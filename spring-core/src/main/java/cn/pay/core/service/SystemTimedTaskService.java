package cn.pay.core.service;

import cn.pay.core.domain.sys.SystemTimedTask;
import cn.pay.core.pojo.qo.SystemTimedTaskQo;
import cn.pay.core.pojo.vo.PageResult;

/**
 * 系统定时任务服务
 * 
 * @author Qiujian
 *
 */
public interface SystemTimedTaskService {

	void saveAndUpdate(SystemTimedTask systemTimedTask);

	/**
	 * 定时任务分页查询
	 * 
	 * @param qo
	 * @return
	 */
	PageResult listQuery(SystemTimedTaskQo qo);

	/**
	 * 根据id查询定时任务信息
	 * 
	 * @param id
	 * @return
	 */
	SystemTimedTask get(Long id);

	/**
	 * 删除定时任务
	 * 
	 * @param id
	 */
	void delete(Long id);

}
