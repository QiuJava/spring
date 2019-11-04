package com.example.config.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.entity.Employee;
import com.example.service.EmployeeServiceImpl;

/**
 * 应用启动监听
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Configuration
public class ContextStartListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private EmployeeServiceImpl employeeService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 系统启动创建超级管理员

		// 判断数据库中是否有超级管理员,没有新建一个
		boolean hasAdmin = employeeService.hasAdmin();
		if (hasAdmin) {
			return;
		}

		Employee employee = new Employee();
		employee.setUsername("admin");
		employee.setPassword(passwordEncoder.encode("123456"));
		employee.setEmail("719749187@qq.com");
		employee.setNickname("超级管理员");
		employee.setStatus(Employee.NORMAL_STATUS);
		employee.setSuperAdmin(Employee.IS_ADMIN);
		employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
		employee.setEmployeeType(Employee.ADMIN_TYPE);
		employee.setEmployeeNumber("000");
		Date date = new Date();
		employee.setCreateTime(date);
		employee.setUpdateTime(date);
		employee.setIntro("超级管理员");
		employeeService.save(employee);
	}

}
