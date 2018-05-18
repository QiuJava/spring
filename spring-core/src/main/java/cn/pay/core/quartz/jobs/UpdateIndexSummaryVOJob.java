package cn.pay.core.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.pay.core.service.IndexService;

@Component
public class UpdateIndexSummaryVOJob implements Job {

	@Autowired
	private IndexService indexService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		indexService.updateIndexSummaryVO();
	}

}
