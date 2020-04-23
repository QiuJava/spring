package com.example.config.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Employee;
import com.example.service.DataDictionaryServiceImpl;
import com.example.service.EmployeeServiceImpl;
import com.example.service.PermissionServiceImpl;
import com.example.util.StrUtil;

/**
 * 应用启动监听
 * 
 * @author Qiujian
 *
 */
@Configuration
public class ContextStartListener implements ApplicationListener<ContextRefreshedEvent> {

	public static final String PEMISSION_MAP = "PEMISSION_MAP";
	public static final String DATA_DICTIONARY_LIST = "DATA_DICTIONARY_LIST";

	@Autowired
	private EmployeeServiceImpl employeeService;

	@Autowired
	private PermissionServiceImpl permissionService;
	
	@Autowired
	private DataDictionaryServiceImpl dataDictionaryService;
	
	@Value("${spring.mail.username}")
	private String username;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		permissionService.settingPermissionMap();
		dataDictionaryService.settingDataDictionaryList();

		// 判断数据库中是否有超级管理员,没有新建一个
		boolean hasSuperAdmin = employeeService.hasByEmployeeType(Employee.SUPER_ADMIN_TYPE);
		if (hasSuperAdmin) {
			return;
		}
		Employee employee = new Employee();
		employee.setEmployeeName(Employee.INIT);
		employee.setEmailAddress(username);
		employee.setEmployeeType(Employee.SUPER_ADMIN_TYPE);
		employee.setPhoneNumber(StrUtil.EMPTY_TEXT);
		employee.setAge(0);
		employee.setEntryTime(new Date());
		employee.setPositionId(0);
		employee.setIdCardNo(StrUtil.EMPTY_TEXT);
		employee.setBankCard(StrUtil.EMPTY_TEXT);
		employee.setDepartmentId(0);
		employee.setEmployeeDynamic(Employee.ON_DUTY_DYNAMIC);
		employee.setGender(Employee.MAN);
		employeeService.save(employee);
	}

}
