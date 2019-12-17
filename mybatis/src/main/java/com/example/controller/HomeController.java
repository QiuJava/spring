package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Model model) {
		return "home";
	}
}
