package cn.loan.core.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.loan.core.common.BaseResult;
import cn.loan.core.entity.qo.WithdrawQo;
import cn.loan.core.service.AccountService;
import cn.loan.core.service.BankCardService;
import cn.loan.core.service.UserInfoService;
import cn.loan.core.service.WithdrawServcie;
import cn.loan.core.util.StringUtil;

/**
 * 提现控制
 * 
 * @author Qiujian
 * 
 */
@Controller
public class WithdrawController {

	public static final String WEBSITE_WITHDRAW_MAPPING = "/website/withdraw";
	public static final String WEBSITE_WITHDRAW_APPLY_MAPPING = "/website/withdraw/apply";
	public static final String WITHDRAW_APPLY = "withdraw_apply";
	public static final String WITHDRAW_LIST = "withdraw/list";
	public static final String MANAGE_WITHDRAW_AUDIT_MAPPING = "/manage/withdraw/audit";
	public static final String MANAGE_WITHDRAW_MAPPING = "/manage/withdraw";

	@Autowired
	private WithdrawServcie withdrawService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private BankCardService bankCardService;

	@GetMapping(WEBSITE_WITHDRAW_MAPPING)
	public String withdraw(Model model) {
		model.addAttribute(StringUtil.BANK_CARD, bankCardService.getCurrent());
		model.addAttribute(StringUtil.USER_INFO, userInfoService.getCurrent());
		model.addAttribute(StringUtil.ACCOUNT, accountService.getCurrent());
		return WITHDRAW_APPLY;
	}

	@PostMapping(WEBSITE_WITHDRAW_APPLY_MAPPING)
	@ResponseBody
	public BaseResult apply(BigDecimal moneyAmount) {
		withdrawService.apply(moneyAmount);
		return BaseResult.ok(StringUtil.EMPTY);
	}

	@GetMapping(MANAGE_WITHDRAW_MAPPING)
	public String withdraw(@ModelAttribute(StringUtil.QO) WithdrawQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, withdrawService.pageQuery(qo));
		return "withdraw/list";
	}

	@PostMapping(MANAGE_WITHDRAW_MAPPING)
	public String pageQuery(@ModelAttribute(StringUtil.QO) WithdrawQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, withdrawService.pageQuery(qo));
		return "withdraw/list";
	}

	@PostMapping(MANAGE_WITHDRAW_AUDIT_MAPPING)
	@ResponseBody
	public BaseResult withdrawAudit(Long id, String remark, Integer auditStatus) {
		withdrawService.audit(id, remark, auditStatus);
		return BaseResult.ok(StringUtil.EMPTY);
	}
}
