package com.example.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.common.LogicException;
import com.example.common.PageResult;
import com.example.common.Result;
import com.example.entity.Employee;
import com.example.qo.EmployeeQo;
import com.example.service.EmployeeServiceImpl;
import com.example.service.email.EmailService;
import com.example.util.SecurityContextUtil;
import com.example.util.StrUtil;
import com.github.pagehelper.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * 员工控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
@Slf4j
public class EmployeeController {
	@Autowired
	private EmployeeServiceImpl employeeService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private EmailService emailService;

	@GetMapping("/employee")
	public String employee() {
		return "employee_list";
	}

	@GetMapping("/employee/listByQo")
	@ResponseBody
	public PageResult<Employee> listByQo(EmployeeQo employeeQo) {
		Page<Employee> page = employeeService.listByQo(employeeQo);
		return new PageResult<>(page.getTotal(), page.getResult());
	}

	@PostMapping("/employee/verifyEmail")
	@ResponseBody
	public boolean verifyEmail(String email) {
		if (!email.endsWith("@qq.com")) {
			return false;
		}
		// 邮箱不能重复
		return !employeeService.hasByEmail(email);
	}
	
	
	@PostMapping("/employee/add")
	@ResponseBody
	public Result addEmployee(Employee employee) {
		String username = employee.getUsername();

		String nickname = employee.getNickname();
		Result verifyNickname = this.verifyNickname(nickname);
		if (verifyNickname != null) {
			return verifyNickname;
		}

		String employeeNumber = employee.getEmployeeNumber();
		Result verifyEmployeeNumber = this.verifyEmployeeNumber(employeeNumber);
		if (verifyEmployeeNumber != null) {
			return verifyEmployeeNumber;
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

		// 初始化
		employee.setStatus(Employee.NORMAL_STATUS);
		employee.setSuperAdmin(Employee.IS_NOT_ADMIN);

		employee.setPassword(passwordEncoder
				.encode(new StringBuilder(20).append(employeeNumber).append(Employee.INIT_PASSWORD_SUFFIX)));
		employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
		Date date = new Date();
		employee.setCreateTime(date);
		employee.setUpdateTime(date);

		try {
			boolean hasEmployeeByUsername = employeeService.hasByUsername(username);
			if (hasEmployeeByUsername) {
				return new Result(false, "用户名已存在");
			}

			boolean hasEmployeeByEmployeeNumber = employeeService.hasEmployeeByEmployeeNumber(employeeNumber);
			if (hasEmployeeByEmployeeNumber) {
				return new Result(false, "工号已存在");
			}
			int save = employeeService.save(employee);
			if (save < 1) {
				return new Result(false, "添加失败");
			}
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "添加失败");
		}
		return new Result(true, "添加成功");
	}

	@GetMapping("/resetPassword")
	public Result resetPassword(Employee employee) {

		String employeeNumber = employee.getEmployeeNumber();
		Result verifyEmployeeNumber = this.verifyEmployeeNumber(employeeNumber);
		if (verifyEmployeeNumber != null) {
			return verifyEmployeeNumber;
		}
		String nickname = employee.getNickname();
		Result verifyNickname = this.verifyNickname(nickname);
		if (verifyNickname != null) {
			return verifyNickname;
		}

		// 校验邮箱
		String email = employee.getEmail();

		try {
			// 只有超级管理员才有重重置密码的权限
			if (SecurityContextUtil.getCurrentEmployee().getSuperAdmin() == Employee.IS_ADMIN) {
				// 重置
				int resetPassword = employeeService.resetPassword(employee);
				if (resetPassword < 1) {
					return new Result(false, "重置失败");
				}
			} else {
				return new Result(false, "重置失败");
			}

		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "重置失败");
		}
		// 发送邮件
		emailService.sendResetPasswordSuccessMail(employeeNumber, email, nickname);
		return new Result(true, "重置成功");
	}

	@PostMapping("/employee/changePassword")
	@ResponseBody
	public Result changePassword(String username, String password, String newPassword) {

		if (StrUtil.noText(password)) {
			return new Result(false, "原密码不能为空");
		} else if (!password.matches(StrUtil.PASSWORD_REGEX)) {
			return new Result(false, "原密码格式不正确");
		}

		if (StrUtil.noText(newPassword)) {
			return new Result(false, "新密码不能为空");
		} else if (!newPassword.matches(StrUtil.PASSWORD_REGEX)) {
			return new Result(false, "新密码格式不正确");
		}

		try {
			int changePassword = employeeService.changePassword(username, password, newPassword);
			if (changePassword != 1) {
				return new Result(false, "修改失败");
			}
			// 修改成功逻辑
			SecurityContextUtil.logout();
			return new Result(true, "修改成功");
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "修改失败");
		}
	}

	private Result verifyEmployeeNumber(String employeeNumber) {
		if (StrUtil.noText(employeeNumber)) {
			return new Result(false, "工号不能为空");
		} else if (StrUtil.isNotEmployeeNumber(employeeNumber)) {
			return new Result(false, "工号格式不正确");
		}
		return null;
	}

	private Result verifyNickname(String nickname) {
		if (StrUtil.isContainSpecialChar(nickname)) {
			return new Result(false, "昵称不能含有特殊字符");
		}
		return null;
	}

	@PostMapping("/employee/verifyUsername")
	@ResponseBody
	public boolean verifyUsername(String username) {
		if (StrUtil.isContainSpecialChar(username)) {
			return false;
		}
		return !employeeService.hasByUsername(username);
	}

}
