package cn.qj.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.core.pojo.qo.RechargeQo;
import cn.qj.core.pojo.vo.AjaxResult;
import cn.qj.core.service.CompanyBankInfoService;
import cn.qj.core.service.RechargeService;

/**
 * 充值相关
 * 
 * @author Administrator
 *
 */
@Controller
public class RechargeController {

	@Autowired
	private CompanyBankInfoService companyBankInfoService;
	@Autowired
	private RechargeService service;

	@RequestMapping("/recharge")
	public String recharge(@ModelAttribute("qo") RechargeQo qo, Model model) {
		model.addAttribute("banks", companyBankInfoService.list());
		model.addAttribute("pageResult", service.list(qo));
		return "recharge/list";
	}

	@RequestMapping("/recharge/audit")
	@ResponseBody
	public AjaxResult rechargeAudit(Long id, String remark, int state) {
		AjaxResult result = new AjaxResult();
		service.audit(id, remark, state);
		result.setSuccess(true);
		return result;
	}
}
