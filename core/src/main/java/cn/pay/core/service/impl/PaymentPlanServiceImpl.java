package cn.pay.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.PaymentPlanRepository;
import cn.pay.core.entity.business.PaymentPlan;
import cn.pay.core.service.PaymentPlanService;

/**
 * 还款计划服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class PaymentPlanServiceImpl implements PaymentPlanService {
	@Autowired
	private PaymentPlanRepository repository;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void saveAndUpdate(PaymentPlan paymentPlan) {
		repository.saveAndFlush(paymentPlan);
	}

}
