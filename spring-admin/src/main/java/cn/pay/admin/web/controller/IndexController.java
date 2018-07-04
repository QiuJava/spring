package cn.pay.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.obj.vo.AjaxResult;
import cn.pay.core.util.HttpSessionContext;
import cn.pay.core.util.StringUtil;

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

	@RequestMapping("/loginInfo/ajax")
	@ResponseBody
	public AjaxResult ajax(HttpServletRequest request) {
		String msg = (String)request.getAttribute("msg");
		if (StringUtil.hasLength(msg)) {
			return new AjaxResult(msg);
		}
		return new AjaxResult(true, "登录成功");
	}

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
