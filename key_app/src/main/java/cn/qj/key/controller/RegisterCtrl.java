package cn.qj.key.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.key.entity.LoginUser;
import cn.qj.key.service.LoginUserService;
import cn.qj.key.util.BaseResult;
import cn.qj.key.util.LogicException;
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
		if (!StrUtil.lenValidator(username, 2, 8)) {
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

	@GetMapping("/register/checkPhoneNum")
	public BaseResult checkPhoneNum(String phoneNum) {
		if (StrUtil.isEmpty(phoneNum)) {
			return BaseResult.err400("手机号码不能传空");
		}
		boolean isPattern = StrUtil.isPattern(phoneNum, StrUtil.PHONE_PATTERN);
		if (!isPattern || !StrUtil.lenValidator(phoneNum, 11, 11)) {
			return BaseResult.err400("手机号码不正确");
		}
		try {
			boolean isExist = loginUserService.isExistPhoneNum(phoneNum);
			if (isExist) {
				return BaseResult.err400("该手机号码已注册");
			}
			return BaseResult.ok("可以注册");
		} catch (Exception e) {
			log.error("获取手机号码否存注册出现异常", e);
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
		if (!isPattern || !StrUtil.lenValidator(phoneNum, 11, 11)) {
			return BaseResult.err400("手机号码不正确");
		}

		// 2. 判断手机号是否存在redis中
		try {
			loginUserService.sendAuthCode(phoneNum);
			return BaseResult.ok("发送成功");
		} catch (Exception e) {
			if (e instanceof LogicException) {
				return BaseResult.err400(e.getMessage());
			}
			log.error("发送手机验证码出现异常", e);
			return BaseResult.err500();
		}

	}

	@PostMapping("/register")
	public BaseResult register(LoginUser loginUser) {
		// 注册信息校验
		if (StrUtil.isEmpty(loginUser.getUsername())) {
			return BaseResult.err400("用户名不能传空");
		}
		if (!StrUtil.lenValidator(loginUser.getUsername(), 2, 8)) {
			return BaseResult.err400("用户名格式不正确");
		}
		if (StrUtil.isEmpty(loginUser.getPhoneNum())) {
			return BaseResult.err400("手机号码不能传空");
		}
		boolean isPattern = StrUtil.isPattern(loginUser.getPhoneNum(), StrUtil.PHONE_PATTERN);
		if (!isPattern || !StrUtil.lenValidator(loginUser.getPhoneNum(), 11, 11)) {
			return BaseResult.err400("手机号码不正确");
		}
		if (StrUtil.isEmpty(loginUser.getAuthCode())) {
			return BaseResult.err400("验证码不能传空");
		}
		if (!StrUtil.lenValidator(loginUser.getAuthCode(), 6, 6)) {
			return BaseResult.err400("验证码格式不正确");
		}

		if (StrUtil.isEmpty(loginUser.getPassword())) {
			return BaseResult.err400("密码不能传空");
		}
		if (!StrUtil.lenValidator(loginUser.getPassword(), 6, 16)) {
			return BaseResult.err400("密码格式不正确");
		}

		if (loginUser.getRegisterSource() < 1 || loginUser.getRegisterSource() > 3) {
			return BaseResult.err400("请传正确的注册来源");
		}

		try {
			loginUserService.register(loginUser);
			return BaseResult.ok("注册成功");
		} catch (Exception e) {
			if (e instanceof LogicException) {
				return BaseResult.err400(e.getMessage());
			}
			log.error("用户注册出现异常", e);
			return BaseResult.err500();
		}

	}

}
