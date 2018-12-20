package cn.qj.key.config.timing.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务
 * 
 * @author Qiujian
 * @date 2018/12/17
 */
@Component
public class TestJob implements Job {

	private static Logger log = LoggerFactory.getLogger(TestJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("============================测试Job===================================");
	}

}
