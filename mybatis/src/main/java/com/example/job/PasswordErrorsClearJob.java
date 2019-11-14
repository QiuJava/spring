package com.example.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.example.service.EmployeeServiceImpl;

/**
 * 密码错误次数清零任务
 * 
 * @author Qiu Jian
 *
 */
public class PasswordErrorsClearJob extends QuartzJobBean {

	@Autowired
	private EmployeeServiceImpl employeeService;

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		employeeService.passwordErrorsClear();
	}
}