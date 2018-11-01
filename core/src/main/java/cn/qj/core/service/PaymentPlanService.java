package cn.qj.core.service;

import cn.qj.core.entity.PaymentPlan;

/**
 * 收款计划服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public interface PaymentPlanService {

	/**
	 * 保存或更新
	 * 
	 * @param paymentPlan
	 */
	void saveAndUpdate(PaymentPlan paymentPlan);

}
