package cn.pay.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.obj.annotation.NoRequiredLogin;
import cn.pay.core.obj.vo.AjaxResult;
import cn.pay.core.service.EmailVerifyService;

/**
 * 邮箱验证相关
 * 
 * @author Administrator
 *
 */
@Controller
public class EmailVerifyController {
	@Autowired
	private EmailVerifyService service;

	@NoRequiredLogin
	@RequestMapping("/email/verify")
	public String verifyMail(String verify, Model model) {
		if (verify != null) {
			service.verifyEmail(verify);
			model.addAttribute("success", true);
			model.addAttribute("msg", "验证成功");
		} else {
			model.addAttribute("success", false);
			model.addAttribute("msg", "验证失败");
		}
		return "checkmail_result";
	}

	@RequestMapping("/email/send")
	@ResponseBody
	public AjaxResult send(String email) {
		service.send(email);
		return new AjaxResult(true, "");
	}
}
