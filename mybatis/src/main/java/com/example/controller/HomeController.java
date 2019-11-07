package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Result;

/**
 * 首页控制器
 * 
 * @author Qiu Jian
 *
 */
@RestController
public class HomeController {

	@GetMapping("/")
	public Result home() {
		return new Result(true, "首页");
	}
}
