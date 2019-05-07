package cn.qj.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.common.BaseResult;

/**
 * Redis 控制器
 * 
 * @author Qiujian
 * @date 2019年4月29日
 *
 */
@RestController
public class RedisController {

	@GetMapping("/redis/value")
	public BaseResult value() {
		return BaseResult.ok("", "");

	}
}
