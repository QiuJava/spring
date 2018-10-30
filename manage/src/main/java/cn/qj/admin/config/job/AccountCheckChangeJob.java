package cn.qj.admin.config.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.qj.core.service.AccountService;

/**
 * 账户信息防篡改检查
 * 
 * @author Qiujian
 * @date 2018/10/30
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
