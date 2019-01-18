package cn.qj.loan.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.core.common.BaseResult;
import cn.qj.core.common.LogicException;
import cn.qj.core.consts.SysConst;
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

	private final Logger log = LoggerFactory.getLogger(LoginInfoController.class);

	@Autowired
	private LoginInfoService service;

	@RequestMapping("/ajax")
	public BaseResult ajax(HttpServletRequest request) {
		String msg = (String) request.getAttribute(SysConst.LOGIN_ERR_MSG);
		if (StringUtil.hasLength(msg)) {
			return BaseResult.err400(msg);
		}
		return BaseResult.ok("登录成功");
	}

	@RequestMapping("/register")
	public BaseResult register(String username, String password) {
		if (!StringUtil.hasLength(username)) {
			return BaseResult.err400("请输入用户名");
		}
		if (!StringUtil.betweenLength(username, 2, 8)) {
			return BaseResult.err400("用户名不合法");
		}
		if (!StringUtil.hasLength(password)) {
			return BaseResult.err400("请输入密码");
		}
		if (!StringUtil.betweenLength(password, 6, 16)) {
			return BaseResult.err400("密码不合法");
		}
		try {
			service.register(username, password);
		} catch (Exception e) {
			if (e instanceof LogicException) {
				return BaseResult.err400(e.getMessage());
			}
			log.error("注册出现异常", e);
			return BaseResult.err500();
		}
		return BaseResult.ok("注册成功");
	}

	@RequestMapping("/isExist")
	public BaseResult isExist(String username) {
		BaseResult result = new BaseResult();
		try {
			boolean isExist = service.isExistByUsername(username);
			result.setSuccess(isExist);
			return result;
		} catch (Exception e) {
			log.error("根据用户名查询用户出现异常", e);
			return BaseResult.err500();
		}
	}

}
