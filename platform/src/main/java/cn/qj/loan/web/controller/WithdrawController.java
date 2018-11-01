package cn.qj.loan.web.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.core.common.AjaxResult;
import cn.qj.core.service.AccountService;
import cn.qj.core.service.UserBankInfoService;
import cn.qj.core.service.UserInfoService;
import cn.qj.core.service.WithdrawServcie;
import cn.qj.core.util.HttpServletContext;

/**
 * 提现控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Controller
public class WithdrawController {
	public static final String WITHDRAW_APPLY = "withdraw_apply";

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
		Long id = HttpServletContext.getCurrentLoginInfo().getId();
		model.addAttribute("bankInfo", userBankInfoService.getByLoginInfoId(id));
		model.addAttribute("userInfo", userInfoService.get(id));
		model.addAttribute("account", accountService.get(id));
		return WITHDRAW_APPLY;
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
