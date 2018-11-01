package cn.qj.admin.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Controller
public class HomeController {

	@RequestMapping("/home")
	public String home() {
		return "main";
	}

}
