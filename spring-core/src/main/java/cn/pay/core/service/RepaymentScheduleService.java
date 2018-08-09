package cn.pay.core.service;

import cn.pay.core.domain.business.RepaymentSchedule;
import cn.pay.core.pojo.qo.RepaymentScheduleQo;
import cn.pay.core.pojo.vo.PageResult;

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
