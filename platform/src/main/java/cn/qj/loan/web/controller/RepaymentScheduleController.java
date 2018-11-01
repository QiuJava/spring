package cn.qj.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.core.common.AjaxResult;
import cn.qj.core.pojo.qo.RepaymentScheduleQo;
import cn.qj.core.service.AccountService;
import cn.qj.core.service.RepaymentScheduleService;
import cn.qj.core.util.HttpServletContext;

/**
 * 还款计划控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
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
		Long id = HttpServletContext.getCurrentLoginInfo().getId();
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
