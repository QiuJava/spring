package cn.loan.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.loan.core.common.BaseResult;
import cn.loan.core.service.LoginUserService;
import cn.loan.core.util.StringUtil;

/**
 * 登录用户控制器
 * 
 * @author qiujian
 *
 */
@Controller
public class LoginUserController {
	public static final String MANAGE_MAPPING = "/manage";
	public static final String MANAGE_LOGIN_MAPPING = "/manage/login";
	public static final String MANAGE_LOGINUSER_LOGINRESULT_MAPPING = "/manage/loginUser/loginResult";
	public static final String MANAGE_MAIN_MAPPING = "/manage/main";
	public static final String MANAGE_LOGIN = "manage-login";
	public static final String MANAGE_MAIN = "manage-main";

	public static final String WEBSITE_MAPPING = "/website";
	public static final String WEBSITE_LOGIN_MAPPING = "/website/login";
	public static final String WEBSITE_LOGINUSER_LOGINRESULT_MAPPING = "/website/loginUser/loginResult";
	public static final String WEBSITE_REGISTER_MAPPING = "/website/register";
	public static final String WEBSITE_LOGINUSER_REGISTER_MAPPING = "/website/loginUser/register";
	public static final String WEBSITE_LOGINUSER_USERNAMEEXIST_MAPPING = "/website/loginUser/usernameExist";
	public static final String WEBSITE_REGISTER = "website-register";
	public static final String WEBSITE_LOGIN = "website-login";

	@Autowired
	private LoginUserService loginUserService;

	@GetMapping(MANAGE_MAPPING)
	public String manage() {
		return MANAGE_LOGIN;
	}

	@GetMapping(MANAGE_LOGIN_MAPPING)
	public String manageLogin() {
		return MANAGE_LOGIN;
	}

	@PostMapping(MANAGE_LOGINUSER_LOGINRESULT_MAPPING)
	@ResponseBody
	public BaseResult manageLoginResult(HttpServletRequest request) {
		String msg = request.getAttribute(StringUtil.MSG).toString();
		if (StringUtil.hasLength(msg)) {
			return BaseResult.err(msg);
		}
		return BaseResult.ok(msg);
	}

	@GetMapping(MANAGE_MAIN_MAPPING)
	public String main() {
		return MANAGE_MAIN;
	}

	@GetMapping(WEBSITE_MAPPING)
	public String website() {
		return WEBSITE_LOGIN;
	}

	@GetMapping(WEBSITE_LOGIN_MAPPING)
	public String websiteLogin() {
		return WEBSITE_LOGIN;
	}

	@PostMapping(WEBSITE_LOGINUSER_LOGINRESULT_MAPPING)
	@ResponseBody
	public BaseResult websiteLoginResult(HttpServletRequest request) {
		String msg = request.getAttribute(StringUtil.MSG).toString();
		if (StringUtil.hasLength(msg)) {
			return BaseResult.err(msg);
		}
		return BaseResult.ok(msg);
	}

	@GetMapping(WEBSITE_REGISTER_MAPPING)
	public String register() {
		return WEBSITE_REGISTER;
	}

	@PostMapping(WEBSITE_LOGINUSER_REGISTER_MAPPING)
	@ResponseBody
	public BaseResult loginUserRegister(String username, String password) {
		loginUserService.register(username, password);
		return BaseResult.ok(StringUtil.EMPTY);
	}

	@GetMapping(WEBSITE_LOGINUSER_USERNAMEEXIST_MAPPING)
	@ResponseBody
	public BaseResult usernameExist(String username) {
		boolean exist = loginUserService.existUserByUsername(username);
		return BaseResult.ok(StringUtil.EMPTY, exist);
	}
}
