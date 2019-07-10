package cn.qj.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.core.common.BaseResult;
import cn.qj.core.entity.UserInfo;
import cn.qj.core.service.SendSmsService;
import cn.qj.core.service.SystemDictionaryItemService;
import cn.qj.core.service.UserInfoService;
import cn.qj.core.util.HttpSessionUtil;

/**
 * 用户信息控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
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
		Long id = HttpSessionUtil.getCurrentLoginInfo().getId();
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
	public BaseResult basicInfoSave(UserInfo userInfo) {
		BaseResult result = new BaseResult();
		service.saveBasicInfo(userInfo);
		result.setSuccess(true);
		return result;
	}

	@RequestMapping("/bindPhone")
	@ResponseBody
	public BaseResult bindPhone(String phoneNumber, String verifyCode) {
		service.bind(phoneNumber, verifyCode);
		return new BaseResult(true, "绑定成功", 200);
	}

	@RequestMapping("/verifyCode")
	@ResponseBody
	public BaseResult verifyCode(String phoneNumber) {
		sendSmsService.verifyCode(phoneNumber);
		return new BaseResult(true, "发送成功", 200);
	}

}
