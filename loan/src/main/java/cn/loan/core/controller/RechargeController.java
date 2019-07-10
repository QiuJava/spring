package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.loan.core.common.BaseResult;
import cn.loan.core.entity.Recharge;
import cn.loan.core.entity.qo.RechargeQo;
import cn.loan.core.service.CompanyBankCardService;
import cn.loan.core.service.RechargeService;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;

/**
 * 充值控制
 * 
 * @author Qiujian
 * 
 */
@Controller
public class RechargeController {

	public static final String RECHARGE = "recharge";
	public static final String RECHARGE_LIST = "recharge_list";
	public static final String RECHARGE_URL_LIST = "recharge/list";
	public static final String WEBSITE_RECHARGE_MAPPING = "/website/recharge";
	public static final String MANAGE_RECHARGE_MAPPING = "/manage/recharge";
	public static final String MANAGE_RECHARGE_AUDIT_MAPPING = "/manage/recharge/audit";
	public static final String WEBSITE_RECHARGE_APPLY_MAPPING = "/website/recharge/apply";
	public static final String WEBSITE_RECHARGE_PAGEQUERY_MAPPING = "/website/recharge/pageQuery";

	@Autowired
	private CompanyBankCardService companyBankCardService;
	@Autowired
	private RechargeService rechargeService;

	@GetMapping(WEBSITE_RECHARGE_MAPPING)
	public String recharge(Model model) {
		// 查询出所有的银行账户信息
		model.addAttribute(StringUtil.COMPANY_BANKCARDS, companyBankCardService.listAll());
		return RECHARGE;
	}

	@PostMapping(WEBSITE_RECHARGE_APPLY_MAPPING)
	@ResponseBody
	public BaseResult apply(Recharge recharge) {
		rechargeService.apply(recharge);
		return BaseResult.ok(StringUtil.EMPTY);
	}

	@GetMapping(WEBSITE_RECHARGE_PAGEQUERY_MAPPING)
	public String pageQuery(@ModelAttribute(StringUtil.QO) RechargeQo qo, Model model) {
		qo.setSubmitter(SecurityContextUtil.getCurrentUser());
		model.addAttribute(StringUtil.PAGE_RESULT, rechargeService.pageQuery(qo));
		return RECHARGE_LIST;
	}

	@PostMapping(WEBSITE_RECHARGE_PAGEQUERY_MAPPING)
	public String pageQueryPost(@ModelAttribute(StringUtil.QO) RechargeQo qo, Model model) {
		qo.setSubmitter(SecurityContextUtil.getCurrentUser());
		model.addAttribute(StringUtil.PAGE_RESULT, rechargeService.pageQuery(qo));
		return RECHARGE_LIST;
	}

	@GetMapping(MANAGE_RECHARGE_MAPPING)
	public String recharge(@ModelAttribute(StringUtil.QO) RechargeQo qo, Model model) {
		model.addAttribute(StringUtil.COMPANY_BANKCARDS, companyBankCardService.listAll());
		model.addAttribute(StringUtil.PAGE_RESULT, rechargeService.pageQuery(qo));
		return RECHARGE_URL_LIST;
	}

	@PostMapping(MANAGE_RECHARGE_MAPPING)
	public String pageQueryManage(@ModelAttribute(StringUtil.QO) RechargeQo qo, Model model) {
		model.addAttribute(StringUtil.COMPANY_BANKCARDS, companyBankCardService.listAll());
		model.addAttribute(StringUtil.PAGE_RESULT, rechargeService.pageQuery(qo));
		return RECHARGE_URL_LIST;
	}

	@PostMapping(MANAGE_RECHARGE_AUDIT_MAPPING)
	@ResponseBody
	public BaseResult rechargeAudit(Long id, String remark, Integer auditStatus) {
		rechargeService.audit(id, remark, auditStatus);
		return BaseResult.ok(StringUtil.EMPTY);
	}
}
