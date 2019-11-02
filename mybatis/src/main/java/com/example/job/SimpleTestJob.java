package com.example.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.example.entity.Employee;
import com.example.service.EmployeeServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试任务
 * 
 * @author Qiu Jian
 *
 */
@Slf4j
public class SimpleTestJob extends QuartzJobBean {

	@Autowired
	private EmployeeServiceImpl employeeService;

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		List<Employee> listAll = employeeService.listAll();
		listAll.forEach(employee -> System.out.println(employee));
		log.info("Simple---------------------------任务");
	}
}