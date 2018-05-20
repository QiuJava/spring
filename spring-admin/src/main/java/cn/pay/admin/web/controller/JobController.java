package cn.pay.admin.web.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pay.core.obj.qo.JobQo;
import cn.pay.core.obj.vo.AjaxResult;

@RestController
@RequestMapping("/job")
public class JobController {

	@Autowired
	private Scheduler scheduler;

	@RequestMapping("/add")
	public AjaxResult add(String name, String group, String cronExpression) throws Exception {
		AjaxResult result = new AjaxResult();
		name = "cn.pay.core.quartz.jobs.FailBorrowJob";
		group = "borrow";
		cronExpression = "*/1 * * * * ?";
		// 启动调度器
		scheduler.start();
		Class<?> clz = Class.forName(name);
		Job job = (Job) clz.newInstance();
		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(name, group).build();

		// 表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder)
				.build();

		scheduler.scheduleJob(jobDetail, trigger);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 停用定时任务
	 * 
	 * @throws SchedulerException
	 */
	@RequestMapping("/pause")
	public void pausejob(String name, String group) throws SchedulerException {
		scheduler.pauseJob(JobKey.jobKey(name, group));
	}

	/**
	 * 重新开始定时任务
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/resume")
	public void resumejob(String name, String group) throws Exception {
		scheduler.resumeJob(JobKey.jobKey(name, group));
	}

	/**
	 * 重设定时任务
	 * 
	 * @param name
	 * @param group
	 * @param cronExpression
	 * @throws Exception
	 */
	@RequestMapping("/reschedule")
	public void rescheduleJob(String name, String group, String cronExpression) throws Exception {
		name = "cn.pay.core.quartz.jobs.GetFailBorrowJob";
		group = "borrow";
		cronExpression = "*/30 * * * * ?";
		TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
		// 表达式调度构建器
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		// 按新的cronExpression表达式重新构建trigger
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

		// 按新的trigger重新设置job执行
		scheduler.rescheduleJob(triggerKey, trigger);
	}

	@RequestMapping("/delete")
	public void deletejob(String name, String group) throws Exception {
		name = "cn.pay.core.quartz.jobs.FailBorrowJob";
		group = "borrow";
		scheduler.pauseTrigger(TriggerKey.triggerKey(name, group));
		scheduler.unscheduleJob(TriggerKey.triggerKey(name, group));
		scheduler.deleteJob(JobKey.jobKey(name, group));
	}

	@RequestMapping("/query")
	public PageRequest queryjob(JobQo qo) {
		// TODO
		return null;
	}

}
