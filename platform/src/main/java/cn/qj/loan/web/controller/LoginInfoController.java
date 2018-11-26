package cn.qj.loan.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.core.common.BaseResult;
import cn.qj.core.service.LoginInfoService;
import cn.qj.core.util.StringUtil;

/**
 * 登录信息控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@RestController
@RequestMapping("/loginInfo")
public class LoginInfoController {
	@Autowired
	private LoginInfoService service;

	@RequestMapping("/ajax")
	public BaseResult ajax(HttpServletRequest request) {
		String msg = (String) request.getAttribute("msg");
		if (StringUtil.hasLength(msg)) {
			return new BaseResult(false, msg, 200);
		}
		return new BaseResult(true, "登录成功", 200);
	}

	@RequestMapping("/register")
	public BaseResult register(String username, String password) {
		service.register(username, password);
		return new BaseResult(true, "注册成功", 200);
	}

	@RequestMapping("/isNotExist")
	public BaseResult isNotExist(String username) {
		BaseResult result = new BaseResult();
		result.setSuccess(service.isNotExistByUsername(username));
		return result;
	}

}
