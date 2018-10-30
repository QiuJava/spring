package cn.qj.admin.config.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.qj.core.service.BorrowService;

/**
 * 流标操作
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Component
public class FailBorrowJob implements Job {

	@Autowired
	private BorrowService borrowService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		borrowService.failBorrow();
	}

}
