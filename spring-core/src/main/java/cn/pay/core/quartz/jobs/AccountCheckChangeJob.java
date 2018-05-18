package cn.pay.core.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static Logger logger = LoggerFactory.getLogger(AccountCheckChangeJob.class);

	@Autowired
	private AccountService accountService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("---------------------------用户账户数据库防篡改检查开始-------------------------------");
		accountService.checkAccountChange();
		logger.info("---------------------------用户账户数据库防篡改检查结束-------------------------------");
	}

}
