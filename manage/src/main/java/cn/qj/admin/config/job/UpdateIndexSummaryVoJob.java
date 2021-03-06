package cn.qj.admin.config.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.qj.core.service.IndexService;

/**
 * 更新首页借款汇总
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Component
public class UpdateIndexSummaryVoJob implements Job {

	@Autowired
	private IndexService indexService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		indexService.updateIndexSummaryVO();
	}

}
