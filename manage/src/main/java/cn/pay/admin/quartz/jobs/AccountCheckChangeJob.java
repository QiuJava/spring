package cn.pay.admin.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.pay.core.service.AccountService;

/**
 * 数据库用户账户信息防篡改检查作业
 * 
 * @author Administrator
 *
 */
@Component
public class AccountCheckChangeJob implements Job {

	@Autowired
	private AccountService accountService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		accountService.checkAccountChange();
	}

}
