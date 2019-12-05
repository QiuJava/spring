package com.example.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Employee;
import com.example.service.EmployeeServiceImpl;
import com.example.util.DateTimeUtil;

/**
 * 用户明细服务实现
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private EmployeeServiceImpl employeeService;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeService.getByUsername(username);

		if (employee == null) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}

		if (employee.getPasswordErrors() + 1 >= Employee.MAX_PASSWORD_ERRORS
				&& employee.getStatus() != Employee.LOCK_STATUS) {
			StringBuilder builder = new StringBuilder();
			builder.append("账户已锁定，请").append(DateTimeUtil.LOCK_INTERVAL / 1000).append("秒后再试");
			throw new LockedException(builder.toString());
		}

		if (Employee.LOCK_STATUS == employee.getStatus()) {
			Date lockTime = employee.getLockTime();
			// 过了锁定区间 进行解锁操作
			Date date = new Date();
			if (date.getTime() > lockTime.getTime() + DateTimeUtil.LOCK_INTERVAL) {
				employee.setLockTime(null);
				employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
				employee.setStatus(Employee.NORMAL_STATUS);
				employee.setUpdateTime(date);
				employeeService.updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeById(employee);
			} else {
				long differ = date.getTime() - lockTime.getTime();
				StringBuilder builder = new StringBuilder();
				builder.append("账户已锁定，请").append((DateTimeUtil.LOCK_INTERVAL - differ) / 1000).append("秒后再试");
				throw new LockedException(builder.toString());
			}
		}
		return employee;
	}

}
