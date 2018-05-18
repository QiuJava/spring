package cn.pay.core.service;

import cn.pay.core.domain.business.RepaymentSchedule;
import cn.pay.core.obj.qo.RepaymentScheduleQo;
import cn.pay.core.obj.vo.PageResult;

/**
 * 还款计划
 * 
 * @author Administrator
 *
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
