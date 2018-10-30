package cn.qj.core.service;

import cn.qj.core.entity.PaymentPlan;

/**
 * 收款计划服务
 * 
 * @author Administrator
 *
 */
public interface PaymentPlanService {

	/**
	 * 保存或更新
	 * 
	 * @param paymentPlan
	 */
	void saveAndUpdate(PaymentPlan paymentPlan);

}
