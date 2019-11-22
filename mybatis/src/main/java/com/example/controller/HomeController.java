package com.example.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
@Controller
@Slf4j
public class HomeController {

	@Autowired
	private LoginLogServiceImpl loginLogService;

	@GetMapping("/")
	public String home() {
		EmployeeVo currentEmployeeVo = SecurityContextUtil.getCurrentEmployeeVo();
		String username = currentEmployeeVo.getUsername();
		try {
			Date newestLoginTime = loginLogService.getNewestLoginTimeByUsername(username);
			currentEmployeeVo.setNewestLoginTime(newestLoginTime);
			return "home";
		} catch (Exception e) {
			log.error("系统异常", e);
			return "error";
		}

	}
}
