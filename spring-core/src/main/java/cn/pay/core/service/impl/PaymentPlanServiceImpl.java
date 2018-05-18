package cn.pay.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.PaymentPlanRepository;
import cn.pay.core.domain.business.PaymentPlan;
import cn.pay.core.service.PaymentPlanService;

@Service
public class PaymentPlanServiceImpl implements PaymentPlanService {
	@Autowired
	private PaymentPlanRepository repository;

	@Override
	@Transactional
	public void saveAndUpdate(PaymentPlan paymentPlan) {
		repository.saveAndFlush(paymentPlan);
	}

}
