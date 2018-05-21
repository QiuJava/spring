package cn.pay.admin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pay.core.service.AccountService;

/**
 * 后天系统额外逻辑功能相关服务
 * 
 * @author Administrator
 *
 */
@RestController
public class LogicService {

	@Autowired
	private AccountService accountService;

	@RequestMapping("/account/flush")
	public void flush() {
		accountService.flushAccountVerify();
	}
}
