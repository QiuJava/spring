package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Department;
import com.example.mapper.DepartmentMapper;

/**
 * 部门服务
 * 
 * @author Qiu Jian
 *
 */
@Service
public class DepartmentServiceImpl {

	@Autowired
	private DepartmentMapper departmentMapper;
	
	public List<Department> listAll() {
		return departmentMapper.listAll();
	}
	
	
}
