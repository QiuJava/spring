package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Position;
import com.example.service.PositionServiceImpl;

/**
 * 职位控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
public class PositionController {
	@Autowired
	private PositionServiceImpl positionService;

	@PostMapping("/position/listByDepartmentId")
	@ResponseBody
	public List<Position> listByDepartmentId(Integer departmentId){
		return positionService.listByDepartmentId(departmentId);
	}
}
