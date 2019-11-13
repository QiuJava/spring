package com.example.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.annotation.DataSourceKey;
import com.example.common.LogicException;
import com.example.dto.ChangePasswordDto;
import com.example.dto.EmployeeLockDto;
import com.example.dto.EmployeeLoginErrorDto;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import com.example.model.ResetPasswordModel;
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
	public int updatePasswordErrorsAndUpdateTimeById(EmployeeLoginErrorDto dto) {
		return employeeMapper.updatePasswordErrorsAndUpdateTimeById(dto);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public int updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeById(EmployeeLockDto employeeUnlockDto) {
		return employeeMapper.updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeById(employeeUnlockDto);
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
	public int resetPassword(ResetPasswordModel resetPasswordModel) {
		resetPasswordModel.setPassword(passwordEncoder.encode(new StringBuilder(20)
				.append(resetPasswordModel.getEmployeeNumber()).append(Employee.INIT_PASSWORD_SUFFIX)));
		resetPasswordModel.setUpdateTime(new Date());
		return employeeMapper.updatePasswordAndUpdateTimeByUsernameEmployeeNumber(resetPasswordModel);
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

}
