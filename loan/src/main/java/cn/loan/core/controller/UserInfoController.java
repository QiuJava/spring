package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.loan.core.common.BaseResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.service.UserInfoService;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 用户资料控制器
 * 
 * @author qiujian
 *
 */
@Controller
public class UserInfoController {

	public static final String WEBSITE_USERINFO_MAPPING = "/website/userInfo";
	public static final String WEBSITE_USERINFO_SAVE_MAPPING = "/website/userInfo/save";
	public static final String USERINFO = "userInfo";

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	@GetMapping(WEBSITE_USERINFO_MAPPING)
	public String userInfo(Model model) {
		model.addAttribute(StringUtil.USER_INFO, userInfoService.getCurrent());
		model.addAttribute(StringUtil.EDUCATION_BACKGROUNDS,
				SystemDictionaryUtil.getItems(SystemDictionaryUtil.EDUCATION_BACKGROUND, systemDictionaryHashService));
		model.addAttribute(StringUtil.INCOME_GRADES,
				SystemDictionaryUtil.getItems(SystemDictionaryUtil.INCOME_GRADE, systemDictionaryHashService));
		return USERINFO;
	}

	@PostMapping(WEBSITE_USERINFO_SAVE_MAPPING)
	@ResponseBody
	public BaseResult save(UserInfo userInfo) {
		userInfoService.saveInfo(userInfo);
		return BaseResult.ok(StringUtil.EMPTY);
	}

}
