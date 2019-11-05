package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import com.example.util.DataSourceUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

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

	public Employee get(Long id) {
		return employeeMapper.selectByPrimaryKey(id);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int update(Employee employee) {
		return employeeMapper.updateByPrimaryKey(employee);
	}

	public Page<Employee> listPage(int pageNum, int pageSize) {
		Page<Employee> startPage = PageHelper.startPage(pageNum, pageSize, true);
		return startPage;
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public boolean hasAdmin() {
		return employeeMapper.countBySuperAdmin() > 0;
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
	public Employee getByUsername(String username) {
		return employeeMapper.selectByUsername(username);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsByPrimaryKey(Employee employee) {
		return employeeMapper.updatePasswordErrorsByPrimaryKey(employee);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsAndStatusAndLockTimeByPrimaryKey(Employee employee) {
		return employeeMapper.updatePasswordErrorsAndStatusAndLockTimeByPrimaryKey(employee);
	}

}
