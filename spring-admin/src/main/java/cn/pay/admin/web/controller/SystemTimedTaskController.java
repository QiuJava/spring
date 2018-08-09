package cn.pay.admin.web.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.domain.sys.SystemTimedTask;
import cn.pay.core.pojo.qo.SystemTimedTaskQo;
import cn.pay.core.pojo.vo.PageResult;
import cn.pay.core.service.SystemTimedTaskService;

/**
 * 系统定时作业相关
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/systemTimedTask")
public class SystemTimedTaskController {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private SystemTimedTaskService service;

	/**
	 * 停用定时任务
	 */
	@RequestMapping("/pause")
	public String pause(Long id) throws Exception {
		SystemTimedTask std = service.get(id);
		scheduler.pauseJob(JobKey.jobKey(std.getJobName(), std.getGroupName()));
		std.setStatus(SystemTimedTask.PAUSE);
		service.saveAndUpdate(std);
		return "redirect:/systemTimedTask/list.do";
	}

	/**
	 * 重新开始定时任务
	 */
	@RequestMapping("/resume")
	public String resume(Long id) throws Exception {
		SystemTimedTask std = service.get(id);
		scheduler.resumeJob(JobKey.jobKey(std.getJobName(), std.getGroupName()));
		std.setStatus(SystemTimedTask.NORMAL);
		service.saveAndUpdate(std);
		return "redirect:/systemTimedTask/list.do";
	}

	@RequestMapping("/delete")
	public String delete(Long id) throws Exception {
		SystemTimedTask std = service.get(id);
		scheduler.pauseTrigger(TriggerKey.triggerKey(std.getJobName(), std.getGroupName()));
		scheduler.unscheduleJob(TriggerKey.triggerKey(std.getJobName(), std.getGroupName()));
		scheduler.deleteJob(JobKey.jobKey(std.getJobName(), std.getGroupName()));
		service.delete(id);
		return "redirect:/systemTimedTask/list.do";
	}

	@RequestMapping("/update")
	public String update(SystemTimedTask systemTimedTask) throws Exception {
		String jobName = systemTimedTask.getJobName();
		String groupName = systemTimedTask.getGroupName();
		String cronExpression = systemTimedTask.getCronExpression();

		// 执行计划构建
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

		if (systemTimedTask.getId() != null) {
			// 重新设置该定时任务
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);

		} else {
			// 新开启一个定时任务
			scheduler.start();
			Class<?> clz = Class.forName(jobName);
			Job job = (Job) clz.newInstance();

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(jobName, groupName).build();

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName)
					.withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);

		}

		systemTimedTask.setStatus(SystemTimedTask.NORMAL);
		service.saveAndUpdate(systemTimedTask);
		return "redirect:/systemTimedTask/list.do";
	}

	@RequestMapping("/list")
	public String list(@ModelAttribute("qo") SystemTimedTaskQo qo, Model model) {
		PageResult page = service.listQuery(qo);
		model.addAttribute("page", page);
		return "systemTimedTask/systemTimedTask_list";
	}

}
