package cn.qj.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.admin.config.security.AdminLoginFailureHandler;
import cn.qj.core.common.AjaxResult;
import cn.qj.core.consts.SysConst;
import cn.qj.core.util.StringUtil;

/**
 * 登录信息控制器
 * 
 * @author Qiujian
 * @date 2018/8/13
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
			return new AjaxResult(false, msg, 400);
		}
		return new AjaxResult(true, "登录成功", 200);
	}

}
