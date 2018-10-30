package cn.qj.admin.config.job;

import org.quartz.Job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.qj.core.service.BorrowService;

/**
 * 获取接下来一个小时内可能流标的借款作业
 * 
 * @author Qiujian
 * @date 2018/10/30
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
