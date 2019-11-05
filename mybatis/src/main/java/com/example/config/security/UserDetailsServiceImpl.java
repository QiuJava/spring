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
	private EmployeeServiceImpl employeeSerivce;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeSerivce.getByUsername(username);
		if (employee == null) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}

		int status = employee.getStatus();
		Date lockTime = employee.getLockTime();

		// 判断用户是否锁定 锁定状态抛出异常
		if (Employee.LOCK_STATUS == status) {
			// 过了锁定区间 进行解锁操作
			Date date = new Date();
			if (date.getTime() > lockTime.getTime() + DateTimeUtil.LOCK_INTERVAL) {
				employee.setLockTime(null);
				employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
				employee.setStatus(Employee.NORMAL_STATUS);
				employeeSerivce.updatePasswordErrorsAndStatusAndLockTimeByPrimaryKey(employee);
			} else {
				long differ = date.getTime() - lockTime.getTime();
				StringBuilder builder = new StringBuilder();
				builder.append("账户已锁定，请").append((DateTimeUtil.LOCK_INTERVAL - differ) / 1000).append("秒后再试");
				throw new LockedException(builder.toString());
			}
		}
		// 设置权限
		return employee;
	}

}
