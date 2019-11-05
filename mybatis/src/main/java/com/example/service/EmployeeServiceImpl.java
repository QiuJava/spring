package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
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
	public int updatePasswordErrorsByPrimaryKey(Employee employee) {
		return employeeMapper.updatePasswordErrorsByPrimaryKey(employee);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsAndStatusAndLockTimeByPrimaryKey(Employee employee) {
		return employeeMapper.updatePasswordErrorsAndStatusAndLockTimeByPrimaryKey(employee);
	}

	public Employee getPasswordErrorsAndIdAndStatusByUsername(String username) {
		return employeeMapper.selectPasswordErrorsAndIdAndStatusByUsername(username);
	}

	public boolean hasEmployeeByUsername(String username) {
		return employeeMapper.countByUsername(username) > 0;
	}

}
