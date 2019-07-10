package cn.loan.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.loan.core.entity.ReceiptPlan;
import cn.loan.core.repository.ReceiptPlanDao;

/**
 * 收款计划服务
 * 
 * @author qiujian
 *
 */
@Service
public class ReceiptPlanService {

	@Autowired
	private ReceiptPlanDao receiptPlanDao;

	public void save(ReceiptPlan plan) {
		receiptPlanDao.save(plan);
	}

}
