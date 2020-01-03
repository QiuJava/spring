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
		String employeeNumber = employee.getEmployeeNumber();
		// 初始化
		employee.setStatus(Employee.NORMAL_STATUS);
		employee.setSuperAdmin(Employee.IS_NOT_ADMIN);

		employee.setPassword(passwordEncoder
				.encode(new StringBuilder(20).append(employeeNumber).append(Employee.INIT_PASSWORD_SUFFIX)));
		employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
		Date date = new Date();
		employee.setCreateTime(date);
		employee.setUpdateTime(date);
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

	public boolean hasByUsernameAndId(Long id, String username) {
		if (id != null) {
			String oldUsername = employeeMapper.selectUsernameById(id);
			if (oldUsername.equals(username)) {
				return false;
			}
		}
		return employeeMapper.countByUsername(username) == 1;
	}

	public boolean hasByEmployeeNumberAndId(Long id, String employeeNumber) {
		if (id != null) {
			String oldEmployeeNumber = employeeMapper.selectEmployeeNumberById(id);
			if (oldEmployeeNumber.equals(employeeNumber)) {
				return false;
			}
		}
		return employeeMapper.countByEmployeeNumber(employeeNumber) == 1;
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
		String currentPassword = SecurityContextUtil.getCurrentEmployee().getPassword();
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

	public int deleteEmployeeRoleByRoleId(Long id) {
		return employeeMapper.deleteEmployeeRoleByRoleId(id);
	}

	public Page<Employee> listByQo(EmployeeQo employeeQo) {
		Page<Employee> page = PageHelper.startPage(employeeQo.getPage(), employeeQo.getRows(), employeeQo.getCount());
		employeeMapper.selectByListByQo(employeeQo);
		return page;
	}

	public boolean hasByEmailAndId(Long id, String email) {
		if (id != null) {
			String oldEmail = employeeMapper.selectEmailById(id);
			if (oldEmail.equals(email)) {
				return false;
			}
		}
		return employeeMapper.countByEmail(email) == 1;
	}

	public boolean hasByNicknameAndId(Long id, String nickname) {
		if (id != null) {
			String oldNickname = employeeMapper.selectNicknameById(id);
			if (oldNickname.equals(nickname)) {
				return false;
			}
		}
		return employeeMapper.countByNickname(nickname) == 1;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void delete(Long id) {
		employeeMapper.deleteById(id);
	}

}
