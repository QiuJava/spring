package cn.pay.admin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.service.AccountService;

/**
 * 额外逻辑功能相关服务
 * 
 * @author Administrator
 *
 */
@Controller
public class LogicService {

	@Autowired
	private AccountService accountService;

	@RequestMapping("/account/flush")
	public String flush() {
		accountService.flushAccountVerify();
		return "main";
	}
}
