package com.example.config.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.EmployeeLockDto;
import com.example.dto.EmployeeLoginErrorDto;
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

		int passwordErrors = employee.getPasswordErrors() + 1;
		Date date = new Date();
		if (passwordErrors >= Employee.MAX_PASSWORD_ERRORS && employee.getStatus() != Employee.LOCK_STATUS) {
			// 进入锁定状态
			EmployeeLockDto dto = new EmployeeLockDto();
			dto.setId(employee.getId());
			dto.setStatus(Employee.LOCK_STATUS);
			dto.setLockTime(date);
			dto.setUpdateTime(date);
			dto.setPasswordErrors(passwordErrors);
			employeeService.updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeById(dto);
		} else {
			EmployeeLoginErrorDto dto = new EmployeeLoginErrorDto();
			dto.setId(employee.getId());
			dto.setPasswordErrors(passwordErrors);
			dto.setUpdateTime(date);
			employeeService.updatePasswordErrorsAndUpdateTimeById(dto);
		}

		LoginLog loginLog = new LoginLog();
		loginLog.setLoginType(LoginLog.LOGIN_FAILURE_STATUS);
		loginLog.setRemoteAddress(details.getRemoteAddress());
		loginLog.setCreateTime(date);
		loginLog.setUpdateTime(date);
		loginLog.setUsername(username);
		loginLog.setRemark(event.getException().getMessage());
		loginLogService.save(loginLog);
	}

}
