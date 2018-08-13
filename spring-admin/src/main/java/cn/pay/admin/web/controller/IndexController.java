package cn.pay.admin.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录相关
 * 
 * @author Administrator
 *
 */
@Controller
public class IndexController {

	@RequestMapping("/index")
	public String index() {
		return "main";
	}

}
