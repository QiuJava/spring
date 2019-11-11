package com.example.config.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.entity.Employee;
import com.example.service.EmployeeServiceImpl;
import com.example.service.MenuServiceImpl;

/**
 * 应用启动监听
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Configuration
public class ContextStartListener implements ApplicationListener<ContextRefreshedEvent> {

	public static final String ALL_MENU_KEY = "ALL_MENU_KEY";

	@Autowired
	private EmployeeServiceImpl employeeService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private MenuServiceImpl menuService;

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (valueOperations.get(ALL_MENU_KEY) == null) {
			// 获取所有菜单 放入Redis缓存
			valueOperations.set(ALL_MENU_KEY, menuService.listAll());
		}
		// 判断数据库中是否有超级管理员,没有新建一个
		boolean hasAdmin = employeeService.hasAdmin();
		if (hasAdmin) {
			return;
		}
		Employee employee = new Employee();
		employee.setUsername("admin");
		employee.setPassword(passwordEncoder.encode(
				new StringBuilder(20).append(Employee.INIT_EMPLOYEE_NUMBER).append(Employee.INIT_PASSWORD_SUFFIX)));
		employee.setEmail("719749187@qq.com");
		employee.setNickname("超级管理员");
		employee.setStatus(Employee.NORMAL_STATUS);
		employee.setSuperAdmin(Employee.IS_ADMIN);
		employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
		employee.setEmployeeType(Employee.ADMIN_TYPE);
		employee.setEmployeeNumber(Employee.INIT_EMPLOYEE_NUMBER);
		Date date = new Date();
		employee.setCreateTime(date);
		employee.setUpdateTime(date);
		employee.setIntro("超级管理员");
		employeeService.save(employee);
	}

}
