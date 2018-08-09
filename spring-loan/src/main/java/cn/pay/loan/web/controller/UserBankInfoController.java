package cn.pay.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.domain.business.UserBankInfo;
import cn.pay.core.domain.business.UserInfo;
import cn.pay.core.service.UserBankInfoService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.HttpServletContext;

/**
 * 绑定银行卡相关
 * 
 * @author Administrator
 *
 */
@Controller
public class UserBankInfoController {
	public static final String BANKINFO = "bankInfo";
	public static final String BANKINFO_RESULT = "bankInfo_result";
	
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserBankInfoService service;

	@RequestMapping("/bankInfo")
	public String bankInfo(Model model) {
		Long id = HttpServletContext.getCurrentLoginInfo().getId();
		UserInfo userInfo = userInfoService.get(id);
		if (userInfo.getIsBankBind()) {
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("bankInfo", service.getByLoginInfoId(id));
			return BANKINFO_RESULT;
		}
		model.addAttribute("userInfo", userInfo);
		return BANKINFO;
	}

	@RequestMapping("/bankInfo/save")
	public String save(UserBankInfo userBankInfo) {
		service.save(userBankInfo);
		return "redirect:/bankInfo.do";
	}
}
