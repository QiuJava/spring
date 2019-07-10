package cn.qj.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.core.common.BaseResult;
import cn.qj.core.pojo.qo.WithdrawQo;
import cn.qj.core.service.WithdrawServcie;

/**
 * 提现审核控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
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
	public BaseResult withdrawAudit(Long id, String remark, int state) {
		service.audit(id, remark, state);
		return BaseResult.ok("提现审核成功", null);
	}
}
