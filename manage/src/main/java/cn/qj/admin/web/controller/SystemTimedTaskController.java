package cn.qj.admin.web.controller;

import java.util.Date;

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

import cn.qj.core.common.PageResult;
import cn.qj.core.consts.StatusConst;
import cn.qj.core.entity.SystemTimedTask;
import cn.qj.core.pojo.qo.SystemTimedTaskQo;
import cn.qj.core.service.SystemTimedTaskService;

/**
 * 系统定时任务控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
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
		SystemTimedTask std = service.getSystemTimedTaskById(id);
		scheduler.pauseJob(JobKey.jobKey(std.getJobName(), std.getGroupName()));
		std.setStatus(StatusConst.PAUSE);
		std.setGmtModified(new Date());
		service.updateSystemTimedTask(std);
		return "redirect:/systemTimedTask/pageQuery";
	}

	/**
	 * 重新开始定时任务
	 */
	@RequestMapping("/resume")
	public String resume(Long id) throws Exception {
		SystemTimedTask std = service.getSystemTimedTaskById(id);
		scheduler.resumeJob(JobKey.jobKey(std.getJobName(), std.getGroupName()));
		std.setStatus(StatusConst.NORMAL);
		std.setGmtModified(new Date());
		service.updateSystemTimedTask(std);
		return "redirect:/systemTimedTask/pageQuery";
	}

	@RequestMapping("/delete")
	public String delete(Long id) throws Exception {
		SystemTimedTask std = service.getSystemTimedTaskById(id);
		scheduler.pauseTrigger(TriggerKey.triggerKey(std.getJobName(), std.getGroupName()));
		scheduler.unscheduleJob(TriggerKey.triggerKey(std.getJobName(), std.getGroupName()));
		scheduler.deleteJob(JobKey.jobKey(std.getJobName(), std.getGroupName()));
		service.deleteById(id);
		return "redirect:/systemTimedTask/pageQuery";
	}

	@RequestMapping("/update")
	public String update(SystemTimedTask systemTimedTask) throws Exception {
		String jobName = systemTimedTask.getJobName();
		String groupName = systemTimedTask.getGroupName();
		String cronExpression = systemTimedTask.getCronExpression();

		// 执行计划构建
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		Date currentDate = new Date();
		if (systemTimedTask.getId() != null) {
			SystemTimedTask std = service.getSystemTimedTaskById(systemTimedTask.getId());
			std.setStatus(StatusConst.NORMAL);
			std.setGmtModified(currentDate);
			service.updateSystemTimedTask(std);
			// 重新设置该定时任务
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} else {
			systemTimedTask.setStatus(StatusConst.NORMAL);
			systemTimedTask.setGmtModified(currentDate);
			systemTimedTask.setGmtCreate(currentDate);
			service.saveSystemTimedTask(systemTimedTask);
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
		return "redirect:/systemTimedTask/pageQuery";
	}

	@RequestMapping("/pageQuery")
	public String pageQuerySystemTimedTask(@ModelAttribute("qo") SystemTimedTaskQo qo, Model model) {
		PageResult pageResult = service.pageQuerySystemTimedTask(qo);
		model.addAttribute("pageResult", pageResult);
		return "systemTimedTask/systemTimedTask_list";
	}

}
