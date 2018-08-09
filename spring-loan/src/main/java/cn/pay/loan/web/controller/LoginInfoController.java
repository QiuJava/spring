package cn.pay.loan.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.pojo.vo.AjaxResult;
import cn.pay.core.service.LoginInfoService;
import cn.pay.core.util.HttpServletContext;
import cn.pay.core.util.StringUtil;

/**
 * 登陆相关
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/loginInfo")
public class LoginInfoController {
	@Autowired
	private LoginInfoService service;

	@RequestMapping("/ajax")
	@ResponseBody
	public AjaxResult ajax(HttpServletRequest request) {
		String msg = (String) request.getAttribute("msg");
		if (StringUtil.hasLength(msg)) {
			return new AjaxResult(msg);
		}
		HttpServletContext.setCurrentLoginInfo(HttpServletContext.getLoginInfoBySecurity());
		return new AjaxResult(true, "登录成功");
	}

	@RequestMapping("/register")
	@ResponseBody
	public AjaxResult register(String username, String password) {
		service.register(username, password);
		return new AjaxResult(true, "注册成功");
	}

	@RequestMapping("/isExist")
	@ResponseBody
	public AjaxResult isExist(String username) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setSuccess(service.isExist(username));
		return ajaxResult;
	}

}
