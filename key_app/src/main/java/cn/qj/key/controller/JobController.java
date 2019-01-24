package cn.qj.key.controller;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.key.util.BaseResult;

/**
 * 定时任务控制
 * 
 * @author Qiujian
 * @date 2018/12/18
 */
@RestController
public class JobController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Scheduler scheduler;

	private final String jobName = "cn.qj.key.config.timing.job.TestJob";
	private final String groupName = "test";
	private final String cronExpression = "0 0/1 * * * ?";

	/**
	 * 停用定时任务
	 */
	@PutMapping("/job/pause")
	public BaseResult pause() {
		try {
			scheduler.pauseJob(JobKey.jobKey(jobName, groupName));
			return BaseResult.ok("停用成功");
		} catch (SchedulerException e) {
			log.error("停用定时任务异常", e);
			return BaseResult.err500();
		}
	}

	/**
	 * 重新开始定时任务
	 */
	@PutMapping("/job/resume")
	public BaseResult resume() {
		try {
			scheduler.resumeJob(JobKey.jobKey(jobName, groupName));
			return BaseResult.ok("重启成功");
		} catch (SchedulerException e) {
			log.error("重启定时任务异常", e);
			return BaseResult.err500();
		}
	}

	@DeleteMapping("/job/delete")
	public BaseResult delete() {
		try {
			scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, groupName));
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, groupName));
			scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
			return BaseResult.ok("删除成功");
		} catch (SchedulerException e) {
			log.error("删除定时任务异常", e);
			return BaseResult.err500();
		}
	}

	@PutMapping("/job/update")
	public BaseResult update() {
		// 执行计划构建
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		// 重新设置该定时任务
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
		try {
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
			return BaseResult.ok("更新成功");
		} catch (SchedulerException e) {
			log.error("更新定时任务异常", e);
			return BaseResult.err500();
		}
	}

	@PostMapping("/job/save")
	public BaseResult save() {
		// 执行计划构建
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		// 新开启一个定时任务
		try {
			scheduler.start();
			Class<?> clz = Class.forName(jobName);
			Job job = (Job) clz.newInstance();
			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(jobName, groupName).build();

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName)
					.withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
			return BaseResult.ok("新增成功");
		} catch (SchedulerException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.error("新增定时任务异常", e);
			return BaseResult.err500();
		}

	}

}
