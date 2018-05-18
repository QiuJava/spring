package cn.pay.core.service;

import cn.pay.core.domain.business.PaymentPlan;

/**
 * 收款计划
 * 
 * @author Administrator
 *
 */
public interface PaymentPlanService {
	
	void saveAndUpdate(PaymentPlan paymentPlan);

}
