package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import cn.loan.core.entity.qo.LoginLogQo;
import cn.loan.core.service.LoginLogService;
import cn.loan.core.service.SystemDictionaryItemService;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 登录日志控制器
 * 
 * @author qiujian
 *
 */
@Controller
public class LoginLogController {

	public static final String MANAGE_LOGINLOG_PAGEQUERY_MAPPING = "/manage/loginLog/pageQuery";
	public static final String MANAGE_LOGINLOG_MAPPING = "/manage/loginLog";
	public static final String MANAGE_LOGINLOG_LIST = "loginLog/list";
	public static final String MANAGE_LOGINLOG_LIST_DETAIL = "loginLog/list_detail";
	public static final String WEBSITE_LOGINLOG_PAGEQUERY_MAPPING = "/website/loginLog/pageQuery";
	public static final String WEBSITE_LOGINLOG_MAPPING = "/website/loginLog";
	public static final String WEBSITE_LOGINLOG_LIST = "loginLog_list";

	@Autowired
	private LoginLogService loginLogService;
	@Autowired
	private SystemDictionaryItemService systemDictionaryItemService;

	@PostMapping(MANAGE_LOGINLOG_PAGEQUERY_MAPPING)
	public String pageQuery(LoginLogQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, loginLogService.pageQuery(qo));
		return MANAGE_LOGINLOG_LIST_DETAIL;
	}

	@GetMapping(MANAGE_LOGINLOG_MAPPING)
	public String loginLog(Model model) {
		model.addAttribute(StringUtil.ITEMS, systemDictionaryItemService.list(SystemDictionaryUtil.LOGIN_STATUS));
		return MANAGE_LOGINLOG_LIST;
	}

	@GetMapping(WEBSITE_LOGINLOG_MAPPING)
	public String websiteLoginLog(@ModelAttribute(StringUtil.QO) LoginLogQo qo, Model model) {
		qo.setUsername(SecurityContextUtil.getCurrentUser().getUsername());
		model.addAttribute(StringUtil.PAGE_RESULT, loginLogService.pageQuery(qo));
		model.addAttribute(StringUtil.ITEMS, systemDictionaryItemService.list(SystemDictionaryUtil.LOGIN_STATUS));
		return WEBSITE_LOGINLOG_LIST;
	}

	@PostMapping(WEBSITE_LOGINLOG_PAGEQUERY_MAPPING)
	public String websitePageQuery(@ModelAttribute(StringUtil.QO) LoginLogQo qo, Model model) {
		qo.setUsername(SecurityContextUtil.getCurrentUser().getUsername());
		model.addAttribute(StringUtil.PAGE_RESULT, loginLogService.pageQuery(qo));
		model.addAttribute(StringUtil.ITEMS, systemDictionaryItemService.list(SystemDictionaryUtil.LOGIN_STATUS));
		return WEBSITE_LOGINLOG_LIST;
	}

}
