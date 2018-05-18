package cn.pay.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.obj.qo.RepaymentScheduleQo;
import cn.pay.core.obj.vo.AjaxResult;
import cn.pay.core.service.AccountService;
import cn.pay.core.service.RepaymentScheduleService;
import cn.pay.core.util.HttpSessionContext;

/**
 * 还款相关
 * 
 * @author Administrator
 *
 */
@Controller
public class RepaymentScheduleController {
	@Autowired
	private RepaymentScheduleService service;
	@Autowired
	private AccountService accountService;

	/**
	 * 用户进入还款界面
	 * 
	 * @param qo
	 * @param model
	 * @return
	 */
	@RequestMapping("/repaymentSchedule")
	public String list(@ModelAttribute("qo") RepaymentScheduleQo qo, Model model) {
		Long id = HttpSessionContext.getCurrentLoginInfo().getId();
		qo.setUserId(id);
		model.addAttribute("pageResult", service.list(qo));
		model.addAttribute("account", accountService.get(id));
		return "repaymentSchedule_list";
	}

	@RequestMapping("/repaymentSchedule/repay")
	@ResponseBody
	public AjaxResult repay(Long id) {
		AjaxResult result = new AjaxResult();
		service.repay(id);
		result.setSuccess(true);
		return result;
	}
}
