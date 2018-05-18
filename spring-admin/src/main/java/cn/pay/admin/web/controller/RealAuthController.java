package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.domain.business.RealAuth;
import cn.pay.core.obj.qo.RealAuthQo;
import cn.pay.core.obj.vo.AjaxResult;
import cn.pay.core.obj.vo.PageResult;
import cn.pay.core.service.RealAuthService;

/**
 * 实名认证相关
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/realAuth")
public class RealAuthController {

	@Autowired
	private RealAuthService service;

	@RequestMapping("/page")
	public String page(@ModelAttribute("qo") RealAuthQo qo, Model model) {
		Page<RealAuth> page = service.page(qo);
		PageResult pageResult = new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(),
				qo.getPageSize());
		model.addAttribute("page", pageResult);
		return "realAuth/list";
	}

	@RequestMapping("/audit")
	@ResponseBody
	public AjaxResult audit(Long id, int state, String remark) {
		service.autid(id, state, remark);
		AjaxResult result = new AjaxResult();
		result.setSuccess(true);
		return result;
	}
}
