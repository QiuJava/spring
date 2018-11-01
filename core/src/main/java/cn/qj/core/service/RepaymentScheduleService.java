package cn.qj.core.service;

import cn.qj.core.common.PageResult;
import cn.qj.core.entity.RepaymentSchedule;
import cn.qj.core.pojo.qo.RepaymentScheduleQo;

/**
 * 还款计划服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public interface RepaymentScheduleService {

	/**
	 * 获取还款计划列表 用户页面分页查询
	 * 
	 * @param qo
	 * @return
	 */
	PageResult list(RepaymentScheduleQo qo);

	/**
	 * 还款操作
	 * 
	 * @param id
	 */
	void repay(Long id);

	/**
	 * 保存或更新
	 * 
	 * @param rs
	 */
	void saveAndUpdate(RepaymentSchedule rs);

	/**
	 * 自动还款 给到定时任务
	 */
	void autoRepay();

	/**
	 * 自动还款短信
	 */
	void autoRepaySms();

}
