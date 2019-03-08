package cn.qj.key.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.key.service.LoginUserService;
import cn.qj.key.util.BaseResult;
import cn.qj.key.util.StrUtil;

/**
 * 注册
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@RestController
public class RegisterCtrl {

	private static final Logger log = LoggerFactory.getLogger(RegisterCtrl.class);

	@Autowired
	private LoginUserService loginUserService;

	@GetMapping("/register/checkUsername")
	public BaseResult checkUsername(String username) {
		if (StrUtil.isEmpty(username)) {
			return BaseResult.err400("用户名不能传空");
		}
		if (StrUtil.lenValidator(username, 2, 8)) {
			return BaseResult.err400("用户名格式不正确");
		}
		try {
			boolean isExist = loginUserService.isExistUsername(username);
			if (isExist) {
				return BaseResult.err400("该用户名已存在");
			}
			return BaseResult.ok("可以注册");
		} catch (Exception e) {
			log.error("获取用户名是否存在出现异常", e);
			return BaseResult.err500();
		}
	}

	@GetMapping("/register/sendAuthCode")
	public BaseResult sendAuthCode(String phoneNum) {
		if (StrUtil.isEmpty(phoneNum)) {
			return BaseResult.err400("手机号码不能传空");
		}
		// 1. 判断手机好是否是正确手机号
		boolean isPattern = StrUtil.isPattern(phoneNum, StrUtil.PHONE_PATTERN);
		if (!isPattern || StrUtil.lenValidator(phoneNum, 11, 11)) {
			return BaseResult.err400("手机号码不正确");
		}

		// 2. 判断手机号是否存在redis中
		// 在redis中拿到发送时间跟当前时间作对比 看是否超过一分钟
		// 不在redis中 生成验证码 发送短信到网关平台 成功把手机号验证码有效时间放到redis 失败则提示发送失败

		return null;
	}

}
