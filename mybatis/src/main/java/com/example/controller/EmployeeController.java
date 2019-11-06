package com.example.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Result;
import com.example.entity.Employee;
import com.example.service.EmployeeServiceImpl;
import com.example.util.SecurityContextUtil;
import com.example.util.StrUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 员工控制器
 * 
 * @author Qiu Jian
 *
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
	@Autowired
	private EmployeeServiceImpl employeeService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/addEmployee")
	public Result addEmployee(Employee employee) {
		Result result = new Result(true, "添加成功");
		String username = employee.getUsername();
		if (StrUtil.noText(username)) {
			return new Result(false, "用户名不能为空");
		} else if (username.length() > 20) {
			return new Result(false, "用户名过长");
		} else if (StrUtil.isContainSpecialChar(username)) {
			return new Result(false, "用户名不能含有特殊字符");
		}
		// 校验邮箱
		String email = employee.getEmail();
		if (StrUtil.hasText(email)) {
			if (email.length() > 50) {
				return new Result(false, "邮箱过长");
			}
			if (!email.matches(StrUtil.EMAIL_REGEX)) {
				return new Result(false, "邮箱格式不正确");
			}
		}
		// 校验昵称
		String nickname = employee.getNickname();
		if (StrUtil.hasText(nickname)) {
			if (nickname.length() > 20) {
				return new Result(false, "昵称过长");
			}
			if (StrUtil.isContainSpecialChar(nickname)) {
				return new Result(false, "昵称不能含有特殊字符");
			}
		}

		Integer employeeType = employee.getEmployeeType();
		if (employeeType == null) {
			return new Result(false, "员工类型不能不为空");
		} else if (employeeType.toString().length() > 2) {
			return new Result(false, "员工类型过长");
		}

		String employeeNumber = employee.getEmployeeNumber();
		if (StrUtil.noText(employeeNumber)) {
			return new Result(false, "工号不能为空");
		} else if (!employeeNumber.matches(StrUtil.EMPLOYEE_NUMBER_REGEX)) {
			return new Result(false, "工号格式不正确");
		}

		String intro = employee.getIntro();
		if (StrUtil.hasText(intro)) {
			if (intro.length() > 255) {
				return new Result(false, "简介过长");
			}
			if (StrUtil.isContainSpecialChar(intro)) {
				return new Result(false, "简介不能含有特殊字符");
			}
		}

		boolean hasEmployeeByUsername = employeeService.hasEmployeeByUsername(username);
		if (hasEmployeeByUsername) {
			return new Result(false, "用户名已存在");
		}

		boolean hasEmployeeByEmployeeNumber = employeeService.hasEmployeeByEmployeeNumber(employeeNumber);
		if (hasEmployeeByEmployeeNumber) {
			return new Result(false, "工号已存在");
		}

		// 初始化
		employee.setStatus(Employee.NORMAL_STATUS);
		employee.setSuperAdmin(Employee.IS_NOT_ADMIN);
		employee.setPassword(passwordEncoder.encode(employeeNumber + Employee.INIT_PASSWORD_SUFFIX));
		employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
		Date date = new Date();
		employee.setCreateTime(date);
		employee.setUpdateTime(date);

		try {
			employeeService.save(employee);
		} catch (Exception e) {
			result.setSucceed(false);
			result.setMsg("添加失败");
			log.error("系统异常", e);
		}
		return result;
	}

	@GetMapping("/resetPassowrd")
	public Result resetPassowrd(Employee employee) {
		Result result = new Result(true, "重置成功");
		Employee currentEmployee = SecurityContextUtil.getCurrentEmployee();
		String username = employee.getUsername();
		if (StrUtil.noText(username)) {
			return new Result(false, "用户名不能为空");
		} else if (username.length() > 20) {
			return new Result(false, "用户名过长");
		} else if (StrUtil.isContainSpecialChar(username)) {
			return new Result(false, "用户名不能含有特殊字符");
		}
		
		String employeeNumber = employee.getEmployeeNumber();
		if (StrUtil.noText(employeeNumber)) {
			return new Result(false, "工号不能为空");
		} else if (!employeeNumber.matches(StrUtil.EMPLOYEE_NUMBER_REGEX)) {
			return new Result(false, "工号格式不正确");
		}
		try {
			// 只有超级管理员才有重重置密码的权限
			if (currentEmployee != null && currentEmployee.getSuperAdmin() == Employee.IS_ADMIN) {
				// 重置
				employeeService.resetPassowrd(employee);
			}
			result.setSucceed(false);
			result.setMsg("重置失败");
		} catch (Exception e) {
			result.setSucceed(false);
			result.setMsg("重置失败");
			log.error("系统异常", e);
		}
		return result;
	}

}
