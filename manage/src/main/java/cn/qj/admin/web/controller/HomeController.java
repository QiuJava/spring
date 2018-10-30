package cn.qj.admin.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页控制
 * 
 * @author Administrator
 *
 */
@Controller
public class HomeController {

	@RequestMapping("/home")
	public String home() {
		return "main";
	}

}
