package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cn.loan.core.entity.Account;
import cn.loan.core.entity.LoginLog;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.service.AccountService;
import cn.loan.core.service.LoginLogService;
import cn.loan.core.service.UserInfoService;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;

/**
 * 个人中心控制
 * 
 * @author qiujian
 *
 */
@Controller
public class PersonalCenterController {

	public static final String PERSONAL_CENTER = "personalCenter";
	public static final String WEBSITE_PERSONAL_CENTER_MAPPING = "/website/personalCenter";

	@Autowired
	private LoginLogService loginLogService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private AccountService accountService;

	@GetMapping(WEBSITE_PERSONAL_CENTER_MAPPING)
	public String personalCenter(Model model) {
		Account account = accountService.getCurrent();
		UserInfo userInfo = userInfoService.getCurrent();
		LoginLog log = loginLogService.getNewest(SecurityContextUtil.getCurrentUser().getUsername());
		// 把对象信息放到model中
		model.addAttribute(StringUtil.USER_INFO, userInfo);
		model.addAttribute(StringUtil.ACCOUNT, account);
		model.addAttribute(StringUtil.LOGIN_LOG, log);
		return PERSONAL_CENTER;
	}

}
