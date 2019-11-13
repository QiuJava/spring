package com.example.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Result;
import com.example.service.LoginLogServiceImpl;
import com.example.util.SecurityContextUtil;
import com.example.vo.EmployeeVo;

import lombok.extern.slf4j.Slf4j;

/**
 * 首页控制器
 * 
 * @author Qiu Jian
 *
 */
@RestController
@Slf4j
public class HomeController {

	@Autowired
	private LoginLogServiceImpl loginLogService;

	@GetMapping("/")
	public Result home() {
		EmployeeVo currentEmployeeVo = SecurityContextUtil.getCurrentEmployeeVo();
		String username = currentEmployeeVo.getUsername();
		try {
			Date newestLoginTime = loginLogService.getNewestLoginTimeByUsername(username);
			currentEmployeeVo.setNewestLoginTime(newestLoginTime);
			return new Result(true, "首页", null, SecurityContextUtil.getCurrentEmployeeVo());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "获取失败");
		}

	}
}
