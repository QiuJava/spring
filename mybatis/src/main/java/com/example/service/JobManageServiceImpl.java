package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.LogicException;
import com.example.entity.JobDetails;
import com.example.qo.JobDetailsQo;
import com.example.util.ListUtil;
import com.example.util.StrUtil;

/**
 * 任务管理服务
 * 
 * @author Qiu Jian
 *
 */
@Service
public class JobManageServiceImpl {

	@Autowired
	private Scheduler scheduler;

	/**
	 * 获取任务列表
	 * 
	 * @param jobDetailsQo
	 * @return
	 * @throws SchedulerException
	 */
	public List<JobDetails> listByQo(JobDetailsQo jobDetailsQo) throws SchedulerException, LogicException {
		List<JobDetails> jobDetailsList = new ArrayList<>();
		String jobGroupName = jobDetailsQo.getJobGroupName();
		// 没有默认查全部
		if (StrUtil.noText(jobGroupName)) {
			List<String> jobGroupNames = scheduler.getJobGroupNames();
			for (String groupName : jobGroupNames) {
				List<JobDetails> listByJobGroupName = this.listByJobGroupName(groupName);
				jobDetailsList.addAll(listByJobGroupName);
			}
		} else {
			List<JobDetails> listByJobGroupName = this.listByJobGroupName(jobGroupName);
			jobDetailsList.addAll(listByJobGroupName);
		}

		// 进行分页
		return ListUtil.page(jobDetailsList, jobDetailsQo.getPageNum(), jobDetailsQo.getPageSize());

	}

	/**
	 * 添加任务
	 * 
	 * @param jobDetails
	 * @throws SchedulerException
	 * @throws ClassNotFoundException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addJob(JobDetails jobDetails) throws SchedulerException, ClassNotFoundException, LogicException {
		// job信息
		String jobName = jobDetails.getJobName();
		String jobGroup = jobDetails.getJobGroupName();
		JobKey jobKey = new JobKey(jobName, jobGroup);
		if (scheduler.checkExists(jobKey)) {
			throw new LogicException("该任务已存在");
		}
		@SuppressWarnings("unchecked")
		Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(jobDetails.getJobClass());
		String jobDescription = jobDetails.getJobDescription();

		TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
		if (scheduler.checkExists(triggerKey)) {
			throw new LogicException("该触发器已存在");
		}

		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).withDescription(jobDescription).build();

		// 创建触发器
		TriggerBuilder<Trigger> newTrigger = TriggerBuilder.newTrigger();
		newTrigger.withIdentity(triggerKey);
		newTrigger.withDescription(jobDetails.getTriggerDescription());
		if (jobDetails.getTriggerStartTime() != null) {
			newTrigger.startAt(jobDetails.getTriggerStartTime());
		}
		if (jobDetails.getTriggerEndTime() != null) {
			newTrigger.endAt(jobDetails.getTriggerEndTime());
		}

		// 根据定时任务类型来设置不同的计划
		int triggerType = jobDetails.getTriggerType();
		if (triggerType == JobDetails.JOB_TYPE_CRON) {
			String cronExpression = jobDetails.getCronExpression();
			if (StrUtil.noText(cronExpression)) {
				throw new LogicException("Cron表达式不能为空");
			} else if (cronExpression.length() > 120) {
				throw new LogicException("Cron表达式过长");
			}
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobDetails.getCronExpression())
					.withMisfireHandlingInstructionDoNothing();
			newTrigger.withSchedule(cronScheduleBuilder);

		} else if (triggerType == JobDetails.JOB_TYPE_SIMPLE) {
			Long repeatInterval = jobDetails.getRepeatInterval();
			if (repeatInterval == null) {
				throw new LogicException("间隔秒数不能为空");
			} else if (repeatInterval.toString().length() > 12) {
				throw new LogicException("间隔秒数过长");
			}
			Integer repeatCount = jobDetails.getRepeatCount();
			if (repeatCount == null) {
				throw new LogicException("执行次数不能为空");
			} else if (repeatCount.toString().length() > 7) {
				throw new LogicException("执行次数过长");
			}
			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(repeatCount,
					Integer.valueOf(repeatInterval.toString()));
			newTrigger.withSchedule(simpleScheduleBuilder);

		}

		// 添加任务
		scheduler.scheduleJob(jobDetail, newTrigger.build());

	}

	/**
	 * 删除任务
	 * 
	 * @param jobDetails
	 * @throws SchedulerException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteJob(JobDetails jobDetails) throws SchedulerException {
		JobKey jobKey = new JobKey(jobDetails.getJobName(), jobDetails.getJobGroupName());
		scheduler.deleteJob(jobKey);

		TriggerKey triggerKey = new TriggerKey(jobDetails.getJobName(), jobDetails.getJobGroupName());
		scheduler.unscheduleJob(triggerKey);
	}

	/**
	 * 恢复任务
	 * 
	 * @param jobDetails
	 * @throws SchedulerException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void resumeJob(JobDetails jobDetails) throws SchedulerException {
		JobKey jobKey = new JobKey(jobDetails.getJobName(), jobDetails.getJobGroupName());
		scheduler.resumeJob(jobKey);

		TriggerKey triggerKey = new TriggerKey(jobDetails.getJobName(), jobDetails.getJobGroupName());
		scheduler.resumeTrigger(triggerKey);
	}

	/**
	 * 暂停任务
	 * 
	 * @param jobDetails
	 * @throws SchedulerException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void pauseJob(JobDetails jobDetails) throws SchedulerException {
		JobKey jobKey = new JobKey(jobDetails.getJobName(), jobDetails.getJobGroupName());
		scheduler.pauseJob(jobKey);

		TriggerKey triggerKey = new TriggerKey(jobDetails.getJobName(), jobDetails.getJobGroupName());
		scheduler.pauseTrigger(triggerKey);

	}

	private List<JobDetails> listByJobGroupName(String jobGroupName) throws SchedulerException {
		List<JobDetails> jobDetailsList = new ArrayList<>();
		// 获取有所的组
		GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.jobGroupContains(jobGroupName);
		Set<JobKey> jobKeySet = scheduler.getJobKeys(jobKeyGroupMatcher);
		for (JobKey jobKey : jobKeySet) {
			String jobName = jobKey.getName();
			String group = jobKey.getGroup();
			// 获取 job 信息
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			String jobDescription = jobDetail.getDescription();
			String jobClass = jobDetail.getJobClass().getName();
			@SuppressWarnings("unchecked")
			List<Trigger> triggerList = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggerList) {
				JobDetails jobDetails = new JobDetails();
				jobDetails.setJobGroupName(group);
				jobDetails.setJobName(jobName);
				jobDetails.setJobDescription(jobDescription);
				jobDetails.setJobClass(jobClass);
				// 获取 trigger 信息
				String triggerState = scheduler.getTriggerState(trigger.getKey()).toString();
				jobDetails.setTriggerState(triggerState);
				jobDetails.setTriggerDescription(trigger.getDescription());
				jobDetails.setTriggerStartTime(trigger.getStartTime());
				jobDetails.setTriggerEndTime(trigger.getEndTime());
				jobDetails.setTriggerNextFireTime(trigger.getNextFireTime());
				jobDetails.setTriggerPreviousFireTime(trigger.getPreviousFireTime());
				jobDetails.setTriggerFinalFireTime(trigger.getFinalFireTime());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					jobDetails.setCronExpression(cronExpression);
					jobDetails.setTriggerType(JobDetails.JOB_TYPE_CRON);
				}
				if (trigger instanceof SimpleTrigger) {
					SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
					int repeatCount = simpleTrigger.getRepeatCount();
					long repeatInterval = simpleTrigger.getRepeatInterval();
					int triggeredTimes = simpleTrigger.getTimesTriggered();
					jobDetails.setRepeatCount(repeatCount);
					jobDetails.setRepeatInterval(repeatInterval);
					jobDetails.setTriggeredTimes(triggeredTimes);
					jobDetails.setTriggerType(JobDetails.JOB_TYPE_SIMPLE);
				}

				jobDetailsList.add(jobDetails);
			}
		}

		return jobDetailsList;
	}

}
