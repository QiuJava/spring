package com.example.config.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Employee;
import com.example.entity.LoginLog;
import com.example.service.EmployeeServiceImpl;
import com.example.service.LoginLogServiceImpl;

/**
 * 认证失败事件监听
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class AuthenticationFailureEventListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

	@Autowired
	private LoginLogServiceImpl loginLogService;
	@Autowired
	private EmployeeServiceImpl employeeService;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
		Authentication authentication = event.getAuthentication();
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
		String username = authentication.getPrincipal().toString();

		// 如果登录名不是系统中的用户名则跳过
		Employee employee = employeeService.getByUsername(username);
		if (employee == null) {
			return;
		}

		if (!Employee.LOCK_STATUS.equals(employee.getEmployeeStatus())) {
			Date date = new Date();
			int passwordErrors = employee.getPasswordErrors() + 1;

			employee.setPasswordErrors(passwordErrors);
			employee.setUpdateTime(date);
			if (passwordErrors >= Employee.MAX_PASSWORD_ERRORS) {
				// 进入锁定状态 设置锁定时间
				employee.setEmployeeStatus(Employee.LOCK_STATUS);
				employee.setLockingTime(date);
				
				
				Employee newEmployee = new Employee();
				newEmployee.setId(employee.getId());
				newEmployee.setPasswordErrors(passwordErrors);
				newEmployee.setUpdateTime(date);
				newEmployee.setLockingTime(date);
				newEmployee.setEmployeeStatus(Employee.LOCK_STATUS);
				employeeService.updatePasswordErrorsAndEmployeeStatusAndLockingTimeById(newEmployee);
			} else {
				Employee newEmployee = new Employee();
				newEmployee.setId(employee.getId());
				newEmployee.setPasswordErrors(passwordErrors);
				newEmployee.setUpdateTime(date);
				employeeService.updatePasswordErrorsById(newEmployee);
			}

			LoginLog loginLog = new LoginLog();
			loginLog.setLoginStatus(LoginLog.LOGIN_FAILURE_STATUS);
			loginLog.setRemoteAddress(details.getRemoteAddress());
			loginLog.setCreateTime(date);
			loginLog.setEmployeeId(employee.getId());
			loginLog.setRemark(event.getException().getMessage());
			loginLogService.save(loginLog);
		}
	}
}
