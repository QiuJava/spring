
package com.example.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.LogicException;
import com.example.common.Result;
import com.example.dto.JobKeyDto;
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
		if (StrUtil.noText(jobDetails.getJobClass())) {
			return new Result(false, "任务类名不能为空");
		}
		if (jobDetails.getTriggerType() == null) {
			return new Result(false, "触发器类型不能为空");
		}
		if (StrUtil.noText(jobDetails.getJobName())) {
			return new Result(false, "任务名称不能为空");
		}
		if (StrUtil.noText(jobDetails.getJobGroupName())) {
			return new Result(false, "任务组名称不能为空");
		}
		Result result = new Result(true, "添加成功");

		Date date = new Date();
		jobDetails.setTriggerStartTime(date);
		jobDetails.setTriggerEndTime(DateUtils.addDays(date, 1));
		try {
			jobManageService.addJob(jobDetails);
		} catch (LogicException e) {
			result.setSucceed(false);
			result.setMsg(e.getMessage());
		} catch (ClassNotFoundException e) {
			result.setSucceed(false);
			result.setMsg("任务类名错误");
		} catch (Exception e) {
			result.setSucceed(false);
			result.setMsg("添加失败");
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/listByQo")
	public Result listByQo(JobDetailsQo qo) {
		Result result = new Result(true, "查询成功");
		try {
			List<JobDetails> list = jobManageService.listByQo(qo);
			result.setData(list);
		} catch (LogicException e) {
			result.setSucceed(false);
			result.setMsg(e.getMessage());
		} catch (Exception e) {
			result.setSucceed(false);
			result.setMsg("查询失败");
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/deleteJob")
	public Result deleteJob(JobKeyDto jobKeyDo) {
		if (StrUtil.noText(jobKeyDo.getJobName())) {
			return new Result(false, "任务名称不能为空");
		}
		if (StrUtil.noText(jobKeyDo.getJobGroupName())) {
			return new Result(false, "任务组名称不能为空");
		}
		Result result = new Result(true);
		try {
			jobManageService.deleteJob(jobKeyDo);
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/pauseJob")
	public Result pauseJob(JobKeyDto jobKeyDo) {
		if (StrUtil.noText(jobKeyDo.getJobName())) {
			return new Result(false, "任务名称不能为空");
		}
		if (StrUtil.noText(jobKeyDo.getJobGroupName())) {
			return new Result(false, "任务组名称不能为空");
		}
		Result result = new Result(true);
		try {
			jobManageService.pauseJob(jobKeyDo);
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/resumeJob")
	public Result resumeJob(JobKeyDto jobKeyDo) {
		if (StrUtil.noText(jobKeyDo.getJobName())) {
			return new Result(false, "任务名称不能为空");
		}
		if (StrUtil.noText(jobKeyDo.getJobGroupName())) {
			return new Result(false, "任务组名称不能为空");
		}
		Result result = new Result(true);
		try {
			jobManageService.resumeJob(jobKeyDo);
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}
}
