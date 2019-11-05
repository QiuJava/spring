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
		boolean hasEmployee = employeeService.hasEmployeeByUsername(username);
		if (!hasEmployee) {
			return;
		}

		Employee employee = employeeService.getPasswordErrorsAndIdAndStatusByUsername(username);
		LoginLog loginLog = new LoginLog();
		loginLog.setLoginType(LoginLog.LOGIN_FAILURE_STATUS);
		loginLog.setRemoteAddress(details.getRemoteAddress());
		Date date = new Date();
		loginLog.setCreateTime(date);
		loginLog.setUpdateTime(date);
		loginLog.setUsername(username);
		loginLog.setRemark(event.getException().getMessage());
		loginLogService.save(loginLog);
		employee.setPasswordErrors(employee.getPasswordErrors() + 1);
		if (employee.getPasswordErrors() >= Employee.MAX_PASSWORD_ERRORS
				&& employee.getStatus() != Employee.LOCK_STATUS) {
			// 进入锁定状态
			employee.setStatus(Employee.LOCK_STATUS);
			employee.setLockTime(date);
			employeeService.updatePasswordErrorsAndStatusAndLockTimeByPrimaryKey(employee);
		} else {
			employeeService.updatePasswordErrorsByPrimaryKey(employee);
		}
	}

}
