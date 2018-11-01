package cn.qj.loan.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.core.common.AjaxResult;
import cn.qj.core.service.LoginInfoService;
import cn.qj.core.util.StringUtil;

/**
 * 登陆相关
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/loginInfo")
public class LoginInfoController {
	@Autowired
	private LoginInfoService service;

	@RequestMapping("/ajax")
	public AjaxResult ajax(HttpServletRequest request) {
		String msg = (String) request.getAttribute("msg");
		if (StringUtil.hasLength(msg)) {
			return new AjaxResult(false, msg, 200);
		}
		return new AjaxResult(true, "登录成功", 200);
	}

	@RequestMapping("/register")
	public AjaxResult register(String username, String password) {
		service.register(username, password);
		return new AjaxResult(true, "注册成功", 200);
	}

	@RequestMapping("/isExist")
	public AjaxResult isExist(String username) {
		AjaxResult result = new AjaxResult();
		result.setSuccess(service.isExistByUsername(username));
		return result;
	}

}
