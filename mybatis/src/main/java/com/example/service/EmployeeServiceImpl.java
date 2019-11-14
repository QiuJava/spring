package com.example.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.common.LogicException;
import com.example.dto.ChangePasswordDto;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import com.example.util.DataSourceUtil;
import com.example.util.SecurityContextUtil;
import com.example.vo.EmployeeVo;

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

	public EmployeeVo getEmployeeVoByUsername(String username) {
		return employeeMapper.selectEmployeeVoByUsername(username);
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

	public boolean hasEmployeeByUsername(String username) {
		return employeeMapper.countByUsername(username) > 0;
	}

	@DataSourceKey(DataSourceUtil.SLAVE_ONE_DATASOURCE_KEY)
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
		EmployeeVo currentEmployeeVo = SecurityContextUtil.getCurrentEmployeeVo();
		if (currentEmployeeVo == null) {
			throw new LogicException("请先登录");
		}
		String currentUsername = currentEmployeeVo.getUsername();
		if (!username.equals(currentUsername)) {
			throw new LogicException("用户名不正确");
		}

		String currentPassword = currentEmployeeVo.getPassword();
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

}
