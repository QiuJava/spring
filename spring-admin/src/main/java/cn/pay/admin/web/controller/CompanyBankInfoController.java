package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.domain.business.CompanyBankInfo;
import cn.pay.core.pojo.qo.CompanyBankInfoQo;
import cn.pay.core.pojo.vo.PageResult;
import cn.pay.core.service.CompanyBankInfoService;

/**
 * 平台账户相关
 * 
 * @author Administrator
 *
 */
@Controller
public class CompanyBankInfoController {

	@Autowired
	private CompanyBankInfoService service;

	@RequestMapping("/companyBankInfo")
	public String companyBankInfo(CompanyBankInfoQo qo, Model model) {
		PageResult pageResult = service.page(qo);
		model.addAttribute("pageResult", pageResult);
		return "companyBankInfo/list";
	}

	@RequestMapping("/companyBankInfo/update")
	public String update(CompanyBankInfo info) {
		service.update(info);
		return "redirect:/companyBankInfo.do";
	}
}
