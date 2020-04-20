package com.example.config.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Employee;
import com.example.service.EmployeeServiceImpl;
import com.example.service.PermissionServiceImpl;

/**
 * 应用启动监听
 * 
 * @author Qiujian
 *
 */
@Configuration
public class ContextStartListener implements ApplicationListener<ContextRefreshedEvent> {

	public static final String PEMISSION_MAP = "PEMISSION_MAP";

	@Autowired
	private EmployeeServiceImpl employeeService;

	@Autowired
	private PermissionServiceImpl permissionService;

	@Value("${spring.mail.username}")
	private String username;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		permissionService.settingPermissionMap();

		// 判断数据库中是否有超级管理员,没有新建一个
		boolean hasSuperAdmin = employeeService.hasByEmployeeType(Employee.SUPER_ADMIN_TYPE);
		if (hasSuperAdmin) {
			return;
		}
		Employee employee = new Employee();
		employee.setUsername(Employee.ADMIN);
		employee.setEmail(username);
		employee.setEmployeeType(Employee.SUPER_ADMIN_TYPE);
		employeeService.save(employee);
	}

}
