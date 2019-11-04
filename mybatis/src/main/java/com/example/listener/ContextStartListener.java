package com.example.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.example.service.EmployeeServiceImpl;

/**
 * 应用启动监听
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
public class ContextStartListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private EmployeeServiceImpl employeeService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 系统启动创建超级管理员

		// 判断数据库中是否有超级管理员,没有新建一个
		boolean hasAdmin = employeeService.hasAdmin();
		if (hasAdmin) {
			return;
		}

	}

}
