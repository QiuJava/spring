package cn.pay.loan.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.obj.annotation.NoRequiredLogin;
import cn.pay.core.obj.vo.AjaxResult;
import cn.pay.core.service.LoginInfoService;
import cn.pay.core.util.HttpSessionContext;
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
		String msg = (String)request.getAttribute("msg");
		if (StringUtil.hasLength(msg)) {
			return new AjaxResult(msg);
		}
		HttpSessionContext.setCurrentLoginInfo(HttpSessionContext.getLoginInfoBySecurity());
		return new AjaxResult(true, "登录成功");
	}

	@NoRequiredLogin
	@RequestMapping("/register")
	@ResponseBody
	public AjaxResult register(String username, String password) {
		service.register(username, password);
		return new AjaxResult(true, "注册成功");
	}

	/*@NoRequiredLogin
	@RequestMapping("/login")
	@ResponseBody
	@RequestLimit(count = 5)
	public AjaxResult login(String username, String password, HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		LoginInfo loginInfo = service.login(username, password, ip, LoginInfo.USER);
		if (loginInfo == null) {
			throw new LogicException("账号或密码不正确");
		}
		return new AjaxResult(true, "登陆成功");
	}*/

	//@NoRequiredLogin
	@RequestMapping("/isExist")
	@ResponseBody
	public AjaxResult isExist(String username) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setSuccess(service.isExist(username));
		return ajaxResult;
	}

	/**
	 * 前台用户注销
	 * 
	 * @param response
	 *            响应对象
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/logout")
	@NoRequiredLogin
	@ResponseBody
	public AjaxResult exit(HttpServletResponse response) throws IOException {
		HttpSession session = HttpSessionContext.getHttpSession();
		session.removeAttribute(HttpSessionContext.CURRENT_LOGIN_INFO);
		response.sendRedirect("/login.html");
		return new AjaxResult(true, "注销成功");
	}
	
}
