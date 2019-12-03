package com.example.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.entity.Employee;
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
		Employee currentEmployee = SecurityContextUtil.getCurrentEmployee();
		String username = currentEmployee.getUsername();

		Date newestLoginTime = loginLogService.getNewestLoginTimeByUsername(username);
		model.addAttribute("newestLoginTime", newestLoginTime);

		return "home";
	}
}
