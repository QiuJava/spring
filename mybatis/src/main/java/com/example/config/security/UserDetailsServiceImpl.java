package com.example.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
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
	public Employee loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeService.getByUsername(username);

		if (employee == null) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}

		if (Employee.LOCK_STATUS.equals(employee.getEmployeeStatus())) {
			Date lockTime = employee.getLockingTime();
			// 过了锁定区间 进行解锁操作
			Date date = new Date();
			if (date.getTime() > lockTime.getTime() + DateTimeUtil.LOCK_INTERVAL) {
				employee.setLockingTime(null);
				employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
				employee.setEmployeeStatus(Employee.NORMAL_STATUS);
				employee.setUpdateTime(date);
				
				
				Employee newEmployee = new Employee();
				newEmployee.setId(employee.getId());
				newEmployee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
				newEmployee.setUpdateTime(date);
				newEmployee.setLockingTime(null);
				newEmployee.setEmployeeStatus(Employee.NORMAL_STATUS);
				employeeService.updatePasswordErrorsAndEmployeeStatusAndLockingTimeById(newEmployee);
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
