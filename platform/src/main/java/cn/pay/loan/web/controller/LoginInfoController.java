package cn.pay.loan.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pay.core.pojo.vo.AjaxResult;
import cn.pay.core.service.LoginInfoService;
import cn.pay.core.util.StringUtil;

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
			return new AjaxResult(msg);
		}
		return new AjaxResult(true, "登录成功");
	}

	@RequestMapping("/register")
	public AjaxResult register(String username, String password) {
		service.register(username, password);
		return new AjaxResult(true, "注册成功");
	}

	@RequestMapping("/isExist")
	public AjaxResult isExist(String username) {
		AjaxResult result = new AjaxResult();
		result.setSuccess(service.isExistByUsername(username));
		return result;
	}

}