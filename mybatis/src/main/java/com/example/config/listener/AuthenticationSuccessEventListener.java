package com.example.config.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Employee;
import com.example.entity.LoginLog;
import com.example.service.EmployeeServiceImpl;
import com.example.service.LoginLogServiceImpl;

/**
 * 认证成功事件监听
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	private LoginLogServiceImpl loginLogService;
	@Autowired
	private EmployeeServiceImpl employeeService;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		Authentication authentication = event.getAuthentication();
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
		Employee employee = (Employee) authentication.getPrincipal();

		LoginLog loginLog = new LoginLog();
		loginLog.setLoginType(LoginLog.LOGIN_SUCCESS_STATUS);
		loginLog.setRemoteAddress(details.getRemoteAddress());
		Date date = new Date();
		loginLog.setCreateTime(date);
		loginLog.setUpdateTime(date);
		loginLog.setUsername(employee.getUsername());
		loginLogService.save(loginLog);

		int passwordErrors = employee.getPasswordErrors();
		// 清空失败次数
		if (passwordErrors > 0) {
			employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
			employee.setLockTime(null);
			employee.setStatus(Employee.NORMAL_STATUS);
			employeeService.updatePasswordErrorsAndStatusAndLockTimeByPrimaryKey(employee);
		}
	}

}
