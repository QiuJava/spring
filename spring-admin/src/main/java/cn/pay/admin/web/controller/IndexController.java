package cn.pay.admin.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.util.HttpSessionContext;

/**
 * 登录相关
 * 
 * @author Administrator
 *
 */
@Controller
public class IndexController {
	
	//@Autowired
	//private LoginInfoService service;

	/*@RequestMapping("/loginInfo/login")
	@ResponseBody
	@NoRequiredLogin
	public AjaxResult login(String username, String password, HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		LoginInfo current = service.login(username, password, ip, LoginInfo.MANAGER);
		if (current == null) {
			return new AjaxResult("用户名或密码不正确");
		}
		return new AjaxResult(true, "登陆成功");
	}*/

	@RequestMapping("/index")
	public String index() {
		HttpSessionContext.setCurrentLoginInfo(HttpSessionContext.getLoginInfoBySecurity());
		return "main";
	}
	
	/*
	@RequestMapping("/loginInfo/logout")
	@ResponseBody
	public AjaxResult logout(HttpServletResponse response) throws IOException {
		HttpSession session = HttpSessionContext.getHttpSession();
		session.removeAttribute(HttpSessionContext.CURRENT_LOGIN_INFO);
		session.removeAttribute(HttpSessionContext.SPRING_SECURITY_CONTEXT);
		response.sendRedirect("/login.html");
		return new AjaxResult(true, "安全退出成功");
	}
	*/

}
