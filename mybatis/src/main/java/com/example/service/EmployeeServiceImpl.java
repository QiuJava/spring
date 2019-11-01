package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
/**
 * 员工服务实现
 *
 * @author Qiu Jian
 *
 */
@Service
public class EmployeeServiceImpl  {

	@Autowired
	private EmployeeMapper employeeMapper;

	public List<Employee> listAll() {
		return employeeMapper.selectAll();
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Employee employee) {
		return employeeMapper.insert(employee);
	}

	public Employee get(Long id) {
		return employeeMapper.selectByPrimaryKey(id);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int update(Employee employee) {
		return employeeMapper.updateByPrimaryKeySelective(employee);
	}
	
	public Page<Employee> listPage(int pageNum,int pageSize) {
		Page<Employee> startPage = PageHelper.startPage(pageNum, pageSize, true);
		employeeMapper.selectAll();
		return startPage;
	}
}
