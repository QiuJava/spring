package cn.pay.core.quartz.jobs;

import org.quartz.Job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.pay.core.service.BorrowService;

/**
 * 获取接下来一个小时内可能流标的借款作业
 * 
 * @author Qiu Jian
 *
 */
@Component
public class GetFailBorrowJob implements Job {

	@Autowired
	private BorrowService borrowService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		borrowService.getFailBorrow();
	}

}