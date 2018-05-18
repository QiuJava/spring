package cn.pay.loan.web.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.obj.vo.AjaxResult;
import cn.pay.core.service.AccountService;
import cn.pay.core.service.UserBankInfoService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.service.WithdrawServcie;
import cn.pay.core.util.HttpSessionContext;

/**
 * 提现
 * 
 * @author Administrator
 *
 */
@Controller
public class WithdrawController {
	public static final String withdraw = "withdraw_apply";
	
	@Autowired
	private WithdrawServcie service;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserBankInfoService userBankInfoService;

	@RequestMapping("/withdraw")
	public String withdraw(Model model) {
		// 获取当前登录用户
		Long id = HttpSessionContext.getCurrentLoginInfo().getId();
		model.addAttribute("bankInfo", userBankInfoService.getByLoginInfoId(id));
		model.addAttribute("userInfo", userInfoService.get(id));
		model.addAttribute("account", accountService.get(id));
		return withdraw;
	}

	@RequestMapping("/withdraw/apply")
	@ResponseBody
	public AjaxResult apply(BigDecimal moneyAmount) {
		AjaxResult result = new AjaxResult();
		service.apply(moneyAmount);
		result.setSuccess(true);
		return result;
	}
}
