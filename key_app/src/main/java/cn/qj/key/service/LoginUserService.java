package cn.qj.key.service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import cn.qj.key.dao.LoginUserDao;
import cn.qj.key.entity.LoginUser;
import cn.qj.key.pojo.PhoneNumAuth;
import cn.qj.key.util.DateTimeUtil;
import cn.qj.key.util.LogicException;

/**
 * 用户登录服务
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Service
public class LoginUserService {

	@Autowired
	private LoginUserDao loginUserDao;

	@Autowired
	private SmsService smsService;

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	public boolean isExistUsername(String username) {
		long count = loginUserDao.countByUsername(username);
		return count > 0;
	}

	public boolean isExistPhoneNum(String phoneNum) {
		long count = loginUserDao.countByPhoneNum(phoneNum);
		return count > 0;
	}

	public void sendAuthCode(String phoneNum) {
		PhoneNumAuth auth = (PhoneNumAuth) valueOperations.get(phoneNum);
		Date date = DateTimeUtil.getDate();
		// 在redis中拿到发送时间跟当前时间作对比 看是否超过一分钟
		if (auth != null
				&& date.getTime() / 1000 < auth.getSendTime().getTime() / 1000 + DateTimeUtil.SEND_AUTHCODE_SPACE_SECOND
				&& auth.getAuthCodeType() == PhoneNumAuth.REGISTER_AUTHCODE_TYPE) {
			// 没过再次发送的限制时间
			throw new LogicException("该手机号已发送验证码");
		}

		// 不在redis中 生成验证码 发送短信到网关平台 成功把手机号验证码有效时间放到redis 失败则提示发送失败
		Random random = new Random();
		int nextInt = random.nextInt(999999);
		String authCode = Integer.toString(nextInt);
		smsService.send(phoneNum, authCode);
		auth = new PhoneNumAuth();
		auth.setAuthCode(authCode);
		auth.setSendTime(DateTimeUtil.getDate());
		auth.setValidSecond(DateTimeUtil.AUTHCODE_VALID);
		auth.setAuthCodeType(PhoneNumAuth.REGISTER_AUTHCODE_TYPE);
		valueOperations.set(phoneNum, auth, DateTimeUtil.AUTHCODE_VALID, TimeUnit.SECONDS);
	}

	public void register(LoginUser loginUser) {
		// 判断验证码是否有效
		PhoneNumAuth auth = (PhoneNumAuth) valueOperations.get(loginUser.getPhoneNum());
		Date curDate = DateTimeUtil.getDate();
		if (auth == null
				|| auth.getSendTime().getTime() / 1000 < curDate.getTime() / 1000 - DateTimeUtil.AUTHCODE_VALID) {
			throw new LogicException("手机号码或验证码不正确");
		}
		if (PhoneNumAuth.REGISTER_AUTHCODE_TYPE != auth.getAuthCodeType()) {
			throw new LogicException("手机号码或验证码不正确");
		}
		if (!loginUser.getAuthCode().equals(auth.getAuthCode())) {
			throw new LogicException("手机号码或验证码不正确");
		}

		if (isExistUsername(loginUser.getUsername())) {
			throw new LogicException("该用户名已存在");
		}
		if (isExistPhoneNum(loginUser.getPhoneNum())) {
			throw new LogicException("该手机号码已注册");
		}

		// 保存用户信息
		loginUser.setUserType(LoginUser.USER_TYPE);
		loginUser.setCreateTime(curDate);
		loginUser.setUpdateTime(curDate);
		loginUser.setUserStatus(LoginUser.NORMAL_STATUS);
		loginUserDao.save(loginUser);
	}

}
