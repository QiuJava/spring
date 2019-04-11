package cn.qj.core.web.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试api
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@RestController
public class HelloApi {
	
	@GetMapping("/api/hello")
	public String hello() {
		return "hello";
	}

}
