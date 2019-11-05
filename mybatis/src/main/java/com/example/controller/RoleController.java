package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Result;

/**
 * 角色控制器
 * 
 * @author Qiu Jian
 *
 */
@RestController
@RequestMapping("/role")
public class RoleController {

	@GetMapping("/addRole")
	public Result addRole() {
		Result result = new Result(true, "添加成功");

		return result;
	}
}
