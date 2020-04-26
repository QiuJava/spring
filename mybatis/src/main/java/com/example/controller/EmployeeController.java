package com.example.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.example.util.DateTimeUtil;
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
			return new Result<>(true, "保存成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "保存失败");
		}
	}

	@PostMapping("/employee/resetPassword")
	@ResponseBody
	public Result<?> resetPassword(Employee employee) {
		// 校验邮箱
		String email = employee.getEmailAddress();
		String username = employee.getUsername();
		try {
			emailService.sendResetPasswordSuccessMail(email, username);
			int resetPassword = employeeService.resetPassword(employee.getId());
			if (resetPassword != 1) {
				return new Result<>(false, "重置失败");
			}
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
		qo.exportInit();
		
		Page<Employee> listByQo = employeeService.listByQo(qo);
		List<Employee> result = listByQo.getResult();

		List<Map<String, Object>> dataList = new ArrayList<>(result.size());

		String[] columnPropertyNameArray = new String[] { "employeeName", "emailAddress", "age", "genderName",
				"phoneNumber", "idCardNo", "bankCard", "entryTime", "departmentName", "positionName",
				"employeeStatusName", "employeeTypeName", "employeeDynamicName", "resignationTime",
				"remainingAnnualLeaveDay", "remainingLieuLeaveDay", "socialSecurityFundRatio" };
		String[] headerColumnNameArray = new String[] { "员工姓名", "邮箱", "年龄", "性别", "手机号码", "身份证号码", "银行卡号", "入职时间",
				"所属部门", "职位", "员工状态", "员工类型", "员工动态", "离职时间", "剩余年假天数", "剩余补休天数", "社保公积金比例" };
		for (Employee employee : result) {
			Map<String, Object> data = new HashMap<>(columnPropertyNameArray.length);
			data.put("employeeName", employee.getEmployeeName());
			data.put("emailAddress", employee.getEmailAddress());
			data.put("age", employee.getAge());
			data.put("genderName", employee.getGenderName());
			data.put("phoneNumber", employee.getPhoneNumber());
			data.put("idCardNo", employee.getIdCardNo());
			data.put("bankCard", employee.getBankCard());
			data.put("entryTime",
					employee.getEntryTime() == null ? "" : DateTimeUtil.getDateTimeStr(employee.getEntryTime()));
			data.put("departmentName", employee.getDepartmentName());
			data.put("positionName", employee.getPositionName());
			data.put("employeeStatusName", employee.getEmployeeStatusName());
			data.put("employeeTypeName", employee.getEmployeeTypeName());
			data.put("employeeDynamicName", employee.getEmployeeDynamicName());
			data.put("resignationTime", employee.getResignationTime() == null ? ""
					: DateTimeUtil.getDateTimeStr(employee.getResignationTime()));
			data.put("remainingAnnualLeaveDay", employee.getRemainingAnnualLeaveDay());
			data.put("remainingLieuLeaveDay", employee.getRemainingLieuLeaveDay());
			data.put("socialSecurityFundRatio", employee.getSocialSecurityFundRatio());
			dataList.add(data);
		}

		try {
			String dateString = DateTimeUtil.getDateStr(new Date());
			String fileName = new String("员工信息表".getBytes("UTF-8"), "ISO-8859-1");
			ExeclUtil.exportExecl(response, dataList, headerColumnNameArray, columnPropertyNameArray,
					fileName.concat(dateString));
		} catch (Exception e) {
			log.error("系统异常", e);
		}
	}

}
