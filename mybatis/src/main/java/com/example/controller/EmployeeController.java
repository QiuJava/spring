package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.util.ExeclUtil;
import com.example.util.SecurityContextUtil;
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
	private EmailService emailService;

	@GetMapping("/employee")
	public String employee() {
		return "employee_list";
	}

	@GetMapping("/employee/listByQo")
	@ResponseBody
	public PageResult<Employee> listByQo(EmployeeQo employeeQo) {
		try {
			Page<Employee> page = employeeService.listByQo(employeeQo);
			return new PageResult<>(page.getTotal(), page.getResult());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new PageResult<>(0L, new ArrayList<>());
		}
	}

	@PostMapping("/employee/add")
	@ResponseBody
	public Result<?> add(Employee employee) {
		try {
			employeeService.save(employee);
			return new Result<>(true, "添加成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "添加失败");
		}
	}

	@PostMapping("/employee/resetPassword")
	@ResponseBody
	public Result<?> resetPassword(Employee employee) {
		// 校验邮箱
		String email = employee.getEmailAddress();
		String username = employee.getUsername();
		try {
			int resetPassword = employeeService.resetPassword(employee.getId(), employee.getUsername());
			if (resetPassword != 1) {
				return new Result<>(false, "重置失败");
			}
			// 发送邮件
			emailService.sendResetPasswordSuccessMail(email, username);
			return new Result<>(true, "重置成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "重置失败");
		}
	}

	@PostMapping("/employee/deleteById")
	@ResponseBody
	public Result<?> deleteById(Integer id) {
		try {
			employeeService.deleteById(id);
			return new Result<>(true, "删除成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "删除失败");
		}
	}

	@PostMapping("/employee/changePassword")
	@ResponseBody
	public Result<?> changePassword(Integer employeeId, String password, String newPassword) {
		try {
			int changePassword = employeeService.changePassword(employeeId, password, newPassword);
			if (changePassword != 1) {
				return new Result<>(false, "修改失败");
			}
			// 修改成功逻辑
			SecurityContextUtil.logout();
			return new Result<>(true, "修改成功");
		} catch (LogicException e) {
			return new Result<>(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "修改失败");
		}
	}

	@GetMapping("/employee/export")
	@ResponseBody
	public void export(EmployeeQo qo, HttpServletResponse response) {
		qo.setCount(false);
		qo.setPage(1);
		qo.setRows(0);

		Page<Employee> listByQo = employeeService.listByQo(qo);
		List<Employee> result = listByQo.getResult();

		List<Map<String, Object>> dataList = new ArrayList<>(result.size());

		for (Employee employee : result) {
			Map<String, Object> data = new HashMap<>();
			data.put("employeeName", employee.getEmployeeName());
			dataList.add(data);
		}

		try {
			String fileName = "员工信息表";
			
			ExeclUtil.exportExecl(response, "员工信息", dataList, new String[] { "员工姓名" }, new String[] { "employeeName" },
					new String(fileName.getBytes("UTF-8"),"ISO-8859-1" ) + ".xlsx");
		} catch (Exception e) {
			log.error("系统异常", e);
		}
	}

}
