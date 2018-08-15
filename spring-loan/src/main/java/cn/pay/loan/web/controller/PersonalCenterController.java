package cn.pay.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.domain.business.Account;
import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.service.AccountService;
import cn.pay.core.service.IpLogService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.HttpServletContext;

/**
 * 个人中心相关
 * 
 * @author Administrator
 *
 */
@Controller
public class PersonalCenterController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private IpLogService ipLogService;
	@Autowired
	private UserInfoService userInfoService;

	/**
	 * 跳转到个人中心界面
	 * 
	 * @return
	 */
	@RequestMapping("/personalCenter")
	public String personal(Model model) {
		// 拿到当前登录对象
		LoginInfo info = HttpServletContext.getCurrentLoginInfo();
		Account account = accountService.get(info.getId());
		IpLog ipLog = ipLogService.getNewestIpLogByUsername(info.getUsername());
		// 把对象信息放到model中
		model.addAttribute("userInfo", userInfoService.get(info.getId()));
		model.addAttribute("account", account);
		model.addAttribute("ipLog", ipLog);
		return "personalCenter";
	}
}
