package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import cn.loan.core.entity.CompanyBankCard;
import cn.loan.core.entity.qo.CompanyBankCardQo;
import cn.loan.core.service.CompanyBankCardService;
import cn.loan.core.util.StringUtil;

/**
 * 公司银行卡信息控制器
 * 
 * @author Qiujian
 * 
 */
@Controller
public class CompanyBankCardController {

	public static final String COMPANY_BANKCARD_COMPANY_BANKCARD_LIST = "companyBankCard/companyBankCard_list";
	public static final String MANAGE_COMPANY_BANKCARD_MAPPING = "/manage/companyBankCard";
	public static final String MANAGE_COMPANY_BANKCARD_SAVE_MAPPING = "/manage/companyBankCard/save";
	public static final String REDIRECT_MANAGE_COMPANY_BANKCARD = "redirect:/manage/companyBankCard";

	@Autowired
	private CompanyBankCardService companyBankCardService;

	@GetMapping(MANAGE_COMPANY_BANKCARD_MAPPING)
	public String companyBankCard(CompanyBankCardQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, companyBankCardService.pageQuery(qo));
		return COMPANY_BANKCARD_COMPANY_BANKCARD_LIST;
	}

	@PostMapping(MANAGE_COMPANY_BANKCARD_MAPPING)
	public String pageQuery(CompanyBankCardQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, companyBankCardService.pageQuery(qo));
		return COMPANY_BANKCARD_COMPANY_BANKCARD_LIST;
	}

	@PostMapping(MANAGE_COMPANY_BANKCARD_SAVE_MAPPING)
	public String save(CompanyBankCard bankCard) {
		companyBankCardService.save(bankCard);
		return REDIRECT_MANAGE_COMPANY_BANKCARD;
	}
}
