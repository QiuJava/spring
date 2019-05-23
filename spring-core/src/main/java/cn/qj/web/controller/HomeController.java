package cn.qj.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 * 
 * @author Qiujian
 * @date 2019年4月11日
 *
 */
@Controller
public class HomeController {

	@GetMapping("/")
	public String index() {
		return "/home";
	}

	@GetMapping("/login")
	public String login() {
		return "/login";
	}

	@GetMapping("/403")
	public String error403() {
		return "/error/403";
	}

	@GetMapping("/home")
	public String home() {
		return "/home";
	}
}
