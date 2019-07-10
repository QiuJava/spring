package cn.qj.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.qj.core.entity.UserBankInfo;
import cn.qj.core.entity.UserInfo;
import cn.qj.core.service.UserBankInfoService;
import cn.qj.core.service.UserInfoService;
import cn.qj.core.util.HttpSessionUtil;

/**
 * 用户银行卡控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Controller
public class UserBankInfoController {

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserBankInfoService service;

	@RequestMapping("/bankInfo")
	public String bankInfo(Model model) {
		Long id = HttpSessionUtil.getCurrentLoginInfo().getId();
		UserInfo userInfo = userInfoService.get(id);
		if (userInfo.getIsBankBind()) {
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("bankInfo", service.getByLoginInfoId(id));
			return "bankInfo_result";
		}
		model.addAttribute("userInfo", userInfo);
		return "bankInfo";
	}

	@RequestMapping("/bankInfo/save")
	public String save(UserBankInfo userBankInfo) {
		service.save(userBankInfo);
		return "redirect:/bankInfo.do";
	}
}
