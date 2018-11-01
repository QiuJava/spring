package cn.qj.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.entity.PaymentPlan;
import cn.qj.core.repository.PaymentPlanRepository;
import cn.qj.core.service.PaymentPlanService;

/**
 * 还款计划服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
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
