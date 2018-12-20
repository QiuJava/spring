package cn.qj.key.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试spring mvc
 * 
 * @author Qiujian
 * @date 2018/12/17
 */
@RestController
public class HelloController {
	
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

}
