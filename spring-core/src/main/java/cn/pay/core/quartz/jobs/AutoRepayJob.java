package cn.pay.core.quartz.jobs;

import org.quartz.Job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.pay.core.service.RepaymentScheduleService;

/**
 * 自动还款
 * 
 * @author Administrator
 *
 */
@Component
public class AutoRepayJob implements Job {

	@Autowired
	private RepaymentScheduleService service;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		service.autoRepay();
	}

}
