package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import cn.loan.core.entity.BankCard;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.service.BankCardService;
import cn.loan.core.service.UserInfoService;
import cn.loan.core.util.StringUtil;

/**
 * 银行卡控制
 * 
 * @author Qiujian
 *
 */
@Controller
public class BankCardController {

	public static final String BANKCARD_RESULT = "bankCard_result";
	public static final String REDIRECT_WEBSITE_BANKCARD = "redirect:/website/bankCard";
	public static final String WEBSITE_BANKCARD_MAPPING = "/website/bankCard";
	public static final String WEBSITE_BANKCARD_SAVE_MAPPING = "/website/bankCard/save";

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private BankCardService bankCardService;

	@GetMapping(WEBSITE_BANKCARD_MAPPING)
	public String bankCard(Model model) {
		UserInfo current = userInfoService.getCurrent();
		if (current.isBankCardBind()) {
			model.addAttribute(StringUtil.USER_INFO, current);
			model.addAttribute(StringUtil.BANK_CARD, bankCardService.getCurrent());
			return BANKCARD_RESULT;
		}
		model.addAttribute(StringUtil.USER_INFO, current);
		return StringUtil.BANK_CARD;
	}

	@PostMapping(WEBSITE_BANKCARD_SAVE_MAPPING)
	public String save(BankCard bankCard) {
		bankCardService.save(bankCard);
		return REDIRECT_WEBSITE_BANKCARD;
	}
}
