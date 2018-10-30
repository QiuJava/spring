package cn.qj.admin.config.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.qj.core.service.RepaymentScheduleService;

/**
 * 发送短信通知充值还款
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Component
public class AutoRepaySmsJob implements Job {

	@Autowired
	private RepaymentScheduleService service;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		service.autoRepaySms();
	}

}
