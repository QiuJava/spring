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

	@RequestMapping("/loginInfo/ajax")
	@ResponseBody
	public AjaxResult ajax(HttpServletRequest request) {
		String msg = (String) request.getAttribute("msg");
		if (StringUtil.hasLength(msg)) {
			return new AjaxResult(msg);
		}
		HttpSessionContext.setCurrentLoginInfo(HttpSessionContext.getLoginInfoBySecurity());
		return new AjaxResult(true, "登录成功");
	}

	@RequestMapping("/index")
	public String index() {
		return "main";
	}

}
