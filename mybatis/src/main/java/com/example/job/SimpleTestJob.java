package com.example.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试任务
 * 
 * @author Qiu Jian
 *
 */
@Slf4j
public class SimpleTestJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("Simple---------------------------任务");
	}
}