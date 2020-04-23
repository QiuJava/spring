package com.example.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.LogicException;
import com.example.config.listener.ContextStartListener;
import com.example.entity.DataDictionary;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import com.example.qo.EmployeeQo;
import com.example.util.SecurityContextUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;

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

	@Autowired
	private HashOperations<String, String, Object> hashOperation;

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Employee employee) {

		if (StringUtil.isEmpty(employee.getPassword())) {
			employee.setPassword(passwordEncoder.encode(employee.getUsername()));
		}
		// 初始化
		employee.setEmployeeStatus(Employee.NORMAL_STATUS);
		employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
		Date date = new Date();
		employee.setCreateTime(date);
		employee.setUpdateTime(date);
		return employeeMapper.insertSelective(employee);
	}

	public Employee getByUsername(String username) {
		return employeeMapper.getByEmployeeName(username);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsById(Employee employee) {
		return employeeMapper.updateByPrimaryKeySelective(employee);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsAndEmployeeStatusAndLockingTimeById(Employee employee) {
		return employeeMapper.updateByPrimaryKeySelective(employee);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int resetPassword(Integer employeeId, String employeeName) {
		Employee employee = new Employee();
		employee.setId(employeeId);
		employee.setPassword(passwordEncoder.encode(employeeName));
		employee.setUpdateTime(new Date());
		return employeeMapper.updateByPrimaryKeySelective(employee);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int changePassword(Integer employeeId, String password, String newPassword) {
		// 密码只能自己修改
		String currentPassword = SecurityContextUtil.getCurrentEmployee().getPassword();
		if (!passwordEncoder.matches(password, currentPassword)) {
			throw new LogicException("原密码不正确");
		}
		// 进行修改操作
		Employee employee = new Employee();
		employee.setId(employeeId);
		employee.setPassword(passwordEncoder.encode(newPassword));
		employee.setUpdateTime(new Date());
		return employeeMapper.updateByPrimaryKeySelective(employee);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int passwordErrorsClear() {
		return employeeMapper.updateAllPasswordErrors();
	}

	public int deleteEmployeeRoleByRoleId(Long id) {
		return employeeMapper.deleteEmployeeRoleByRoleId(id);
	}

	public Page<Employee> listByQo(EmployeeQo employeeQo) {
		Page<Employee> page = PageHelper.startPage(employeeQo.getPage(), employeeQo.getRows(), employeeQo.getCount());
		employeeMapper.listByQo(employeeQo);
		page.getResult().forEach(employee -> {
			DataDictionary employeeDynamicDataDictionary = (DataDictionary) hashOperation.get(
					ContextStartListener.DATA_DICTIONARY_LIST,
					DataDictionary.EMPLOYEE_DYNAMIC.concat(employee.getEmployeeDynamic()));
			employee.setEmployeeDynamicName(employeeDynamicDataDictionary.getDataName());

			DataDictionary employeeStatusDataDictionary = (DataDictionary) hashOperation.get(
					ContextStartListener.DATA_DICTIONARY_LIST,
					DataDictionary.EMPLOYEE_STATUS.concat(employee.getEmployeeStatus()));
			employee.setEmployeeStatusName(employeeStatusDataDictionary.getDataName());

			DataDictionary employeeTypeDataDictionary = (DataDictionary) hashOperation.get(
					ContextStartListener.DATA_DICTIONARY_LIST,
					DataDictionary.EMPLOYEE_TYPE.concat(employee.getEmployeeType()));
			employee.setEmployeeTypeName(employeeTypeDataDictionary.getDataName());

			DataDictionary genderDataDictionary = (DataDictionary) hashOperation
					.get(ContextStartListener.DATA_DICTIONARY_LIST, DataDictionary.GENDER.concat(employee.getGender()));
			employee.setGenderName(genderDataDictionary.getDataName());
		});

		return page;
	}

	/**
	 * 业务删除
	 * 
	 * @param id
	 */
	@Transactional(rollbackFor = RuntimeException.class)
	public void deleteById(Integer id) {

		Employee employee = new Employee();
		// 修改为失效状态
		employee.setEmployeeStatus(Employee.INVALID_STATUS);
		employee.setUpdateTime(new Date());
		employee.setId(id);
		employeeMapper.updateByPrimaryKeySelective(employee);
	}

	public boolean hasByEmployeeType(String superAdminType) {
		return employeeMapper.countByEmployeeType(superAdminType) == 1;
	}

}
