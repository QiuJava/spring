package cn.qj.key.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.key.util.LogicException;

/**
 * 异常测试控制器
 * 
 * @author Qiujian
 * @date 2018/12/18
 */
@RestController
public class ExceptionTestController {

	@GetMapping("/exception")
	public String exception() {
		int i = 1 / 0;
		return i + "";
	}

	@GetMapping("/logicException")
	public String logicException() {
		throw new LogicException("逻辑异常");
	}

}
