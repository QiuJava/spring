package com.example.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import com.example.util.DataSourceUtil;

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
	public int resetPassowrd(Employee employee) {
		employee.setPassword(passwordEncoder.encode(employee.getEmployeeNumber() + Employee.INIT_PASSWORD_SUFFIX));
		employee.setUpdateTime(new Date());
		return employeeMapper.updatePasswordAndUpdateTimeByUsername(employee);
	}

}
