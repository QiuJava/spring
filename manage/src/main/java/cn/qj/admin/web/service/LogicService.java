package cn.qj.admin.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.qj.core.service.AccountService;

/**
 * 逻辑服务
 * 
 * @author Qiujian
 * @date 2018/10/30
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
