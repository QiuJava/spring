package cn.pay.core.service;

import cn.pay.core.entity.business.PaymentPlan;

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
