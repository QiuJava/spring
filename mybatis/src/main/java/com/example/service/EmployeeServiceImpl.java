package com.example.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.common.LogicException;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import com.example.util.DataSourceUtil;
import com.example.util.SecurityContextUtil;

/**
 * 员工服务实现
 *
 * @author Qiu Jian
 *
 */
@Service
public class EmployeeServiceImpl {

	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Employee employee) {
		return employeeMapper.insertSelective(employee);
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasAdmin() {
		return employeeMapper.countBySuperAdmin() > 0;
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public Employee getContainAuthoritiesByUsername(String username) {
		return employeeMapper.selectContainAuthoritiesByUsername(username);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsAndUpdateTimeByPrimaryKey(Employee employee) {
		return employeeMapper.updatePasswordErrorsAndUpdateTimeByPrimaryKey(employee);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeByPrimaryKey(Employee employee) {
		return employeeMapper.updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeByPrimaryKey(employee);
	}

	public Employee getPasswordErrorsAndIdAndStatusByUsername(String username) {
		return employeeMapper.selectPasswordErrorsAndIdAndStatusByUsername(username);
	}

	public boolean hasEmployeeByUsername(String username) {
		return employeeMapper.countByUsername(username) > 0;
	}

	public boolean hasEmployeeByEmployeeNumber(String employeeNumber) {
		return employeeMapper.countByEmployeeNumber(employeeNumber) > 0;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int resetPassword(Employee employee) {
		employee.setPassword(passwordEncoder.encode(
				new StringBuilder(20).append(employee.getEmployeeNumber()).append(Employee.INIT_PASSWORD_SUFFIX)));
		employee.setUpdateTime(new Date());
		return employeeMapper.updatePasswordAndUpdateTimeByUsernameEmployeeNumber(employee);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int changePassword(String username, String password, String newPassword) {
		// 密码只能自己修改
		Employee currentEmployee = SecurityContextUtil.getCurrentEmployee();
		if (currentEmployee == null) {
			throw new LogicException("请先登录");
		}
		String currentUsername = currentEmployee.getUsername();
		if (!username.equals(currentUsername)) {
			throw new LogicException("用户名不正确");
		}

		String currentPassword = currentEmployee.getPassword();
		if (!passwordEncoder.matches(password, currentPassword)) {
			throw new LogicException("原密码不正确");
		}

		// 进行修改操作
		String encodePassword = passwordEncoder.encode(newPassword);
		Date updateTime = new Date();
		return employeeMapper.updatePasswordAndUpdateTimeByUsername(username, encodePassword, updateTime);
	}

}
