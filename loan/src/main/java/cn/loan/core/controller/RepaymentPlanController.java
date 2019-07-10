package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.loan.core.common.BaseResult;
import cn.loan.core.entity.qo.RepaymentPlanQo;
import cn.loan.core.service.AccountService;
import cn.loan.core.service.RepaymentPlanService;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;

/**
 * 还款计划控制
 * 
 * @author Qiujian
 * 
 */
@Controller
public class RepaymentPlanController {

	public static final String WEBSITE_REPAYMENT_PLAN_MAPPING = "/website/repaymentPlan";
	public static final String WEBSITE_REPAYMENT_PLAN_REPAYMENT_MAPPING = "/website/repaymentPlan/repayment";
	public static final String REPAYMENTPLAN_LIST = "repaymentPlan_list";

	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private AccountService accountService;

	@GetMapping(WEBSITE_REPAYMENT_PLAN_MAPPING)
	public String repaymentPlan(@ModelAttribute(StringUtil.QO) RepaymentPlanQo qo, Model model) {
		qo.setBorrowerId(SecurityContextUtil.getCurrentUser().getId());
		model.addAttribute(StringUtil.PAGE_RESULT, repaymentPlanService.pageQuery(qo));
		model.addAttribute(StringUtil.ACCOUNT, accountService.getCurrent());
		return REPAYMENTPLAN_LIST;
	}

	@PostMapping(WEBSITE_REPAYMENT_PLAN_MAPPING)
	public String pageQuery(@ModelAttribute(StringUtil.QO) RepaymentPlanQo qo, Model model) {
		qo.setBorrowerId(SecurityContextUtil.getCurrentUser().getId());
		model.addAttribute(StringUtil.PAGE_RESULT, repaymentPlanService.pageQuery(qo));
		model.addAttribute(StringUtil.ACCOUNT, accountService.getCurrent());
		return REPAYMENTPLAN_LIST;
	}

	@PostMapping(WEBSITE_REPAYMENT_PLAN_REPAYMENT_MAPPING)
	@ResponseBody
	public BaseResult repay(Long id) {
		repaymentPlanService.repayment(id);
		return BaseResult.ok(StringUtil.EMPTY);
	}
}
