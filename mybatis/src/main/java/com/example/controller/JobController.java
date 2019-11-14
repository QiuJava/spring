
package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.LogicException;
import com.example.common.Result;
import com.example.entity.JobDetails;
import com.example.qo.JobDetailsQo;
import com.example.service.JobManageServiceImpl;
import com.example.util.StrUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 任务控制器
 * 
 * @author Qiu Jian
 *
 */
@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {

	@Autowired
	private JobManageServiceImpl jobManageService;

	@GetMapping("/addJob")
	public Result addJob(JobDetails jobDetails) {
		String jobClass = jobDetails.getJobClass();
		if (StrUtil.noText(jobClass)) {
			return new Result(false, "任务类名不能为空");
		} else if (jobClass.length() > 250) {
			return new Result(false, "任务类名过长");
		}

		Integer triggerType = jobDetails.getTriggerType();
		if (triggerType == null) {
			return new Result(false, "触发器类型不能为空");
		} else if (triggerType != JobDetails.JOB_TYPE_CRON && triggerType != JobDetails.JOB_TYPE_SIMPLE) {
			return new Result(false, "触发器类型不正确");
		}

		String jobName = jobDetails.getJobName();
		Result verifyJobName = this.verifyJobName(jobName);
		if (verifyJobName != null) {
			return verifyJobName;
		}

		String jobGroupName = jobDetails.getJobGroupName();
		Result verifyJobGroupName = this.verifyJobGroupName(jobGroupName);
		if (verifyJobGroupName != null) {
			return verifyJobGroupName;
		}

		String jobDescription = jobDetails.getJobDescription();
		if (StrUtil.hasText(jobDescription) && jobDescription.length() > 250) {
			return new Result(false, "任务描述过长");
		}

		try {
			jobManageService.addJob(jobDetails);
			return new Result(true, "添加成功");
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (ClassNotFoundException e) {
			return new Result(false, "任务类名错误");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}

	@GetMapping("/listByQo")
	public Result listByQo(JobDetailsQo qo) {

		Result verify = qo.verify();
		if (verify != null) {
			return verify;
		}

		String jobGroupName = qo.getJobGroupName();
		if (StrUtil.hasText(jobGroupName) && jobGroupName.length() > 190) {
			return new Result(false, "任务组名称过长");
		}
		if (StrUtil.hasText(jobGroupName) && StrUtil.isContainSpecialChar(jobGroupName)) {
			return new Result(false, "任务组名称不能含有特殊字符");
		}
		try {
			List<JobDetails> list = jobManageService.listByQo(qo);
			return new Result(true, "查询成功", null, list);
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "查询失败");
		}
	}

	@GetMapping("/deleteJob")
	public Result deleteJob(JobDetails jobDetails) {
		String jobName = jobDetails.getJobName();
		Result verifyJobName = this.verifyJobName(jobName);
		if (verifyJobName != null) {
			return verifyJobName;
		}

		String jobGroupName = jobDetails.getJobGroupName();
		Result verifyJobGroupName = this.verifyJobGroupName(jobGroupName);
		if (verifyJobGroupName != null) {
			return verifyJobGroupName;
		}
		try {
			jobManageService.deleteJob(jobDetails);
			return new Result(true, "删除任务成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "删除任务失败");
		}
	}

	@GetMapping("/pauseJob")
	public Result pauseJob(JobDetails jobDetails) {
		String jobName = jobDetails.getJobName();
		Result verifyJobName = this.verifyJobName(jobName);
		if (verifyJobName != null) {
			return verifyJobName;
		}

		String jobGroupName = jobDetails.getJobGroupName();
		Result verifyJobGroupName = this.verifyJobGroupName(jobGroupName);
		if (verifyJobGroupName != null) {
			return verifyJobGroupName;
		}
		try {
			jobManageService.pauseJob(jobDetails);
			return new Result(true, "暂停任务成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(true, "暂停任务失败");
		}
	}

	@GetMapping("/resumeJob")
	public Result resumeJob(JobDetails jobDetails) {
		String jobName = jobDetails.getJobName();
		Result verifyJobName = this.verifyJobName(jobName);
		if (verifyJobName != null) {
			return verifyJobName;
		}
		String jobGroupName = jobDetails.getJobGroupName();
		Result verifyJobGroupName = this.verifyJobGroupName(jobGroupName);
		if (verifyJobGroupName != null) {
			return verifyJobGroupName;
		}
		try {
			jobManageService.resumeJob(jobDetails);
			return new Result(true, "重启任务成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "重启任务失败");
		}
	}

	private Result verifyJobGroupName(String jobGroupName) {
		if (StrUtil.noText(jobGroupName)) {
			return new Result(false, "任务组名称不能为空");
		} else if (jobGroupName.length() > 190) {
			return new Result(false, "任务组名称过长");
		} else if (StrUtil.isContainSpecialChar(jobGroupName)) {
			return new Result(false, "任务组名称不能含有特殊字符");
		}
		return null;
	}

	private Result verifyJobName(String jobName) {
		if (StrUtil.noText(jobName)) {
			return new Result(false, "任务名称不能为空");
		} else if (jobName.length() > 190) {
			return new Result(false, "任务名称过长");
		} else if (StrUtil.isContainSpecialChar(jobName)) {
			return new Result(false, "任务名称不能含有特殊字符");
		}
		return null;
	}
}
