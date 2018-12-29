package cn.qj.key.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import cn.qj.key.util.WechatUtil;

@Controller
public class JsonController {
	
	@GetMapping("/wechat/jsonFormat")
	public String getJsonFormat() {
		return WechatUtil.MENU_JSON;
	}

}
