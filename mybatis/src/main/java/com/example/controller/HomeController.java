package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.service.LoginLogServiceImpl;
import com.example.util.SecurityContextUtil;

/**
 * 首页控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
public class HomeController {
	
	@Autowired
	private LoginLogServiceImpl loginLogService;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("newestLoginTime", loginLogService.getNewestLoginTimeByEmployeeId(SecurityContextUtil.getCurrentEmployee().getId()));
		return "home";
	}
}
