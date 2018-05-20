package cn.pay.admin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pay.core.service.AccountService;

@RestController
public class LogicService {
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping("/account/flush")
	public void flush() {
		accountService.flushAccountVerify();
	}
}
