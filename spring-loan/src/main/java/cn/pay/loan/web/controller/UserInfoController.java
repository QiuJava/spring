package cn.pay.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.domain.business.UserInfo;
import cn.pay.core.pojo.vo.AjaxResult;
import cn.pay.core.service.SendSmsService;
import cn.pay.core.service.SystemDictionaryItemService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.HttpServletContext;

/**
 * 用户相关信息相关
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/userInfo")
public class UserInfoController {

	@Autowired
	private UserInfoService service;
	@Autowired
	private SystemDictionaryItemService systemDictionaryItemService;
	@Autowired
	private SendSmsService sendSmsService;

	/**
	 * 跳转到个人资料页面
	 * 
	 * @return
	 */
	@RequestMapping("/basic")
	public String basicInfo(Model model) {
		Long id = HttpServletContext.getCurrentLoginInfo().getId();
		// 1.拿到用户基本资料
		model.addAttribute("userInfo", service.get(id));
		// 2.拿到字典对应的明细
		model.addAttribute("educationBackgrounds", systemDictionaryItemService.getBySn("educationBackground"));
		model.addAttribute("incomeGrades", systemDictionaryItemService.getBySn("incomeGrade"));
		model.addAttribute("marriages", systemDictionaryItemService.getBySn("marriage"));
		model.addAttribute("kidCounts", systemDictionaryItemService.getBySn("kidCount"));
		model.addAttribute("houseConditions", systemDictionaryItemService.getBySn("houseCondition"));
		return "userInfo";
	}

	@RequestMapping("/basic/save")
	@ResponseBody
	public AjaxResult basicInfoSave(UserInfo userInfo) {
		AjaxResult result = new AjaxResult();
		service.saveBasicInfo(userInfo);
		result.setSuccess(true);
		return result;
	}

	@RequestMapping("/bindPhone")
	@ResponseBody
	public AjaxResult bindPhone(String phoneNumber, String verifyCode) {
		service.bind(phoneNumber, verifyCode);
		return new AjaxResult(true, "绑定成功");
	}

	@RequestMapping("/verifyCode")
	@ResponseBody
	public AjaxResult verifyCode(String phoneNumber) {
		sendSmsService.verifyCode(phoneNumber);
		return new AjaxResult(true, "发送成功");
	}

}
