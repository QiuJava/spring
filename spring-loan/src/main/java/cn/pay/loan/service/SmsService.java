package cn.pay.loan.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.obj.annotation.NoRequiredLogin;

/**
 * 模拟短信网关系统服务
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/sms")
public class SmsService {

	/**
	 * 网关平台接收其他系统传输过来的短信内容 并执行发送短信到用户手机
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
