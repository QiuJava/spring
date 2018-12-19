package cn.qj.key.controller;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务控制
 * 
 * @author Qiujian
 * @date 2018/12/18
 */
@RestController
public class JobController {

	@Autowired
	private Scheduler scheduler;

	private final String jobName = "cn.qj.key.config.timing.job.TestJob";
	private final String groupName = "test";
	private final String cronExpression = "0 0/1 * * * ?";

	/**
	 * 停用定时任务
	 */
	@PutMapping("/pause")
	public String pause() throws Exception {
		scheduler.pauseJob(JobKey.jobKey(jobName, groupName));
		return null;
	}

	/**
	 * 重新开始定时任务
	 */
	@PutMapping("/resume")
	public String resume() throws Exception {
		scheduler.resumeJob(JobKey.jobKey(jobName, groupName));
		return null;
	}

	@DeleteMapping("/delete")
	public String delete() throws Exception {
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, groupName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, groupName));
		scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
		return null;
	}

	@PutMapping("/update")
	public String update() throws Exception {
		// 执行计划构建
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		// 重新设置该定时任务
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		// 按新的trigger重新设置job执行
		scheduler.rescheduleJob(triggerKey, trigger);
		return null;
	}

	@PostMapping("/save")
	public String save() throws Exception {
		// 执行计划构建
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		// 新开启一个定时任务
		scheduler.start();
		Class<?> clz = Class.forName(jobName);
		Job job = (Job) clz.newInstance();

		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(jobName, groupName).build();

		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(scheduleBuilder)
				.build();

		scheduler.scheduleJob(jobDetail, trigger);
		return null;
	}

}
