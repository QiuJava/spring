package cn.qj.key.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.key.util.WechatUtil;

@RestController
public class JsonController {
	
	@GetMapping("/jsonFormat")
	public String getJsonFormat() {
		return WechatUtil.MENU_JSON;
	}

}
