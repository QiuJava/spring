
package com.example.controller;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Result;
import com.example.entity.Employee;
import com.example.quartz.JobKeyDo;
import com.example.service.EmployeeServiceImpl;
import com.example.service.JobManageService;
import com.example.quartz.JobDetails;
import com.example.quartz.JobDetailsQo;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 测试控制器
 * 
 * @author Qiu Jian
 *
 */
@RestController
@Slf4j
public class TestController {

	@Autowired
	private JobManageService jobManageService;

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private EmployeeServiceImpl employeeService;

	@GetMapping("/addEmployee")
	public Result addEmployee(Employee employee) {
		Result result = new Result();
		result.setSucceed(true);
		try {
			int save = employeeService.save(employee);
			if (save < 1) {
				result.setSucceed(false);
			}
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/addValueCache")
	public Result addValueCache() {
		Result result = new Result();
		result.setSucceed(true);
		try {
			List<Employee> list = employeeService.listAll();
			valueOperations.set("list", list, 1, TimeUnit.MINUTES);
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/addJob")
	public Result addJob() {
		Result result = new Result();
		JobDetails jobDetails = new JobDetails();
		jobDetails.setJobClass("com.example.quartz.job.CronTestJob");
		jobDetails.setTriggerType(JobDetails.JOB_TYPE_CRON);
		jobDetails.setJobName("Cron测试任务");
		jobDetails.setJobGroupName("测试任务组");
		jobDetails.setJobDescription("Cron测试任务描述");
		jobDetails.setCronExpression("0/10 * * * * ? ");
		Date date = new Date();
		jobDetails.setTriggerStartTime(date);
		jobDetails.setTriggerEndTime(DateUtils.addDays(date, 1));
		result.setSucceed(true);
		try {
			jobManageService.addJob(jobDetails);
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/getAllJob")
	public Result getAllJob(JobDetailsQo qo) {
		Result result = new Result();
		qo.setPageNum(1);
		qo.setPageSize(10);
		result.setSucceed(true);
		try {
			List<JobDetails> list = jobManageService.listByQo(qo);
			result.setData(list);
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/deleteJob")
	public Result deleteJob() {
		Result result = new Result();
		JobKeyDo jobKeyDo = new JobKeyDo();
		jobKeyDo.setJobName("Cron测试任务");
		jobKeyDo.setJobGroupName("测试任务组");
		result.setSucceed(true);
		try {
			jobManageService.deleteJob(jobKeyDo);
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/pauseJob")
	public Result pauseJob() {
		Result result = new Result();
		JobKeyDo jobKeyDo = new JobKeyDo();
		jobKeyDo.setJobName("Cron测试任务");
		jobKeyDo.setJobGroupName("测试任务组");
		result.setSucceed(true);
		try {
			jobManageService.pauseJob(jobKeyDo);
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/resumeJob")
	public Result resumeJob() {
		Result result = new Result();
		JobKeyDo jobKeyDo = new JobKeyDo();
		jobKeyDo.setJobName("Cron测试任务");
		jobKeyDo.setJobGroupName("测试任务组");
		result.setSucceed(true);
		try {
			jobManageService.resumeJob(jobKeyDo);
		} catch (Exception e) {
			result.setSucceed(false);
			log.error("系统异常", e);
		}
		return result;
	}
}
