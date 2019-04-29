package cn.qj.core.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.core.common.BaseResult;

@RestController
public class RedisController {

	
	
	
	@GetMapping("/redis/value")
	public BaseResult value() {
		
		return BaseResult.ok("", "");
		
	}
}
