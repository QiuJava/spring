package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Department;
import com.example.service.DepartmentServiceImpl;

/**
 * 部门控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
public class DepartmentController {

	@Autowired
	private DepartmentServiceImpl departmentService;

	@GetMapping("/department/listAll")
	@ResponseBody
	public List<Department> listAll() {
		return departmentService.listAll();
	}
}
