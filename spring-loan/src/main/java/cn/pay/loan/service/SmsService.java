package cn.pay.loan.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.obj.annotation.NoRequiredLogin;

@Controller
@RequestMapping("/sms")
public class SmsService {

	/**
	 * 模拟短信网关系统
	 * 
	 * @param content
	 * @return
	 */
	@NoRequiredLogin
	@RequestMapping("/send")
	@ResponseBody
	public String send(String content) {
		System.out.println(content);
		return "success";
	}

}
