package cn.pay.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.admin.security.AdminLoginFailureHandler;
import cn.pay.core.consts.SysConst;
import cn.pay.core.pojo.vo.AjaxResult;
import cn.pay.core.util.StringUtil;

/**
 * 登录信息控制
 * 
 * @author Qiujian
 * @date 2018年8月13日
 */
@Controller
public class LoginInfoController {

	/**
	 * 登录时信息返回给页面 不需要权限控制
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(SysConst.URL_LOGIN_INFO_AJAX)
	@ResponseBody
	public AjaxResult ajax(HttpServletRequest request) {
		String msg = (String) request.getAttribute(AdminLoginFailureHandler.LOGIN_ERR_MSG);
		if (StringUtil.hasLength(msg)) {
			return new AjaxResult(msg);
		}
		return new AjaxResult(true, "登录成功");
	}
	
}