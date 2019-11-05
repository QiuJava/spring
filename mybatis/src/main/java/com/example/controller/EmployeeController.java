package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Result;
import com.example.entity.Employee;
import com.example.service.EmployeeServiceImpl;

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

	@GetMapping("/addEmployee")
	public Result addEmployee(Employee employee) {
		Result result = new Result(true, "添加成功");
		try {
			employeeService.save(employee);
		} catch (Exception e) {
			result.setSucceed(false);
			result.setMsg("添加失败");
			log.error("系统异常", e);
		}
		return result;
	}
	
}
