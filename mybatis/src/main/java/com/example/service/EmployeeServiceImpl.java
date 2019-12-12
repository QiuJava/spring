package com.example.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.LogicException;
import com.example.dto.ChangePasswordDto;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import com.example.qo.EmployeeQo;
import com.example.util.SecurityContextUtil;
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
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Transactional(rollbackFor = RuntimeException.class)
	public int save(Employee employee) {
		return employeeMapper.insertSelective(employee);
	}

	public boolean hasAdmin() {
		return employeeMapper.countBySuperAdmin() > 0;
	}

	public Employee getByUsername(String username) {
		return employeeMapper.selectByUsername(username);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsAndUpdateTimeById(Employee employee) {
		return employeeMapper.updatePasswordErrorsAndUpdateTimeById(employee);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeById(Employee employee) {
		return employeeMapper.updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeById(employee);
	}

	public Employee getPasswordErrorsAndIdAndStatusByUsername(String username) {
		return employeeMapper.selectPasswordErrorsAndIdAndStatusByUsername(username);
	}

	public boolean hasByUsername(String username) {
		return employeeMapper.countByUsername(username) == 1;
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
		ChangePasswordDto changePasswordDto = new ChangePasswordDto();
		changePasswordDto.setUsername(username);
		changePasswordDto.setEncodePassword(passwordEncoder.encode(newPassword));
		changePasswordDto.setUpdateTime(new Date());
		return employeeMapper.updatePasswordAndUpdateTimeByUsername(changePasswordDto);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int passwordErrorsClear() {
		return employeeMapper.updateAllPasswordErrors();
	}

	public int countEmployeeRoleByRoleId(Long id) {
		return employeeMapper.countEmployeeRoleByRoleId(id);
	}

	public int deleteEmployeeRoleByRoleId(Long id) {
		return employeeMapper.deleteEmployeeRoleByRoleId(id);
	}

	public Page<Employee> listByQo(EmployeeQo employeeQo) {
		Page<Employee> page = PageHelper.startPage(employeeQo.getPage(), employeeQo.getRows(), employeeQo.getCount());
		employeeMapper.selectByListByQo(employeeQo);
		return page;
	}

}
