package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.pojo.qo.WithdrawQo;
import cn.pay.core.pojo.vo.AjaxResult;
import cn.pay.core.service.WithdrawServcie;

/**
 * 提现审核相关
 * 
 * @author Administrator
 *
 */
@Controller
public class WithdrawAuthController {
	@Autowired
	private WithdrawServcie service;

	@RequestMapping("/withdraw")
	public String withdraw(@ModelAttribute("qo") WithdrawQo qo, Model model) {
		model.addAttribute("pageResult", service.list(qo));
		return "withdraw/list";
	}

	@RequestMapping("/withdraw/audit")
	@ResponseBody
	public AjaxResult withdrawAudit(Long id, String remark, int state) {
		AjaxResult result = new AjaxResult();
		service.audit(id, remark, state);
		result.setSuccess(true);
		return result;
	}
}
