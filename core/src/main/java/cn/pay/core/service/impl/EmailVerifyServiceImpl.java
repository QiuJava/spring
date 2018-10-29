package cn.pay.core.service.impl;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.pay.core.dao.EmailVerifyRepository;
import cn.pay.core.entity.business.EmailVerify;
import cn.pay.core.entity.business.UserInfo;
import cn.pay.core.service.EmailVerifyService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.BidStateUtil;
import cn.pay.core.util.HttpServletContext;
import cn.pay.core.util.LogicException;

/**
 * 邮箱校验服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
@Transactional(rollbackFor = { RuntimeException.class })
public class EmailVerifyServiceImpl implements EmailVerifyService {
	@Autowired
	private EmailVerifyRepository repository;

	@Autowired
	private UserInfoService userInfoService;

	@Override
	public void verifyEmail(String verify) {
		if (StringUtils.hasLength(verify)) {
			EmailVerify emailVerify = repository.findByVerify(verify);
			if (emailVerify != null
					&& new Date().before(DateUtils.addDays(emailVerify.getVerifyDate(), EmailVerify.VALIDITY_DAY))) {
				UserInfo userInfo = userInfoService.get(emailVerify.getUserId());
				if (!userInfo.getIsBindEmail()) {
					userInfo.setEmail(emailVerify.getEmail());
					userInfo.addState(BidStateUtil.OP_BIND_EMAIL);
					userInfoService.update(userInfo);
				}
			} else {
				throw new LogicException("验证码无效或过期");
			}
		}
	}

	@Override
	public void send(String email) {
		UserInfo userInfo = userInfoService.get(HttpServletContext.getCurrentLoginInfo().getId());
		// 如果用户已经绑定邮箱,直接略过
		if (!userInfo.getIsBindEmail()) {
			// 创建一个绑定用户的验证对象
			EmailVerify emailVerity = new EmailVerify(email);
			// 保存mailVerity
			EmailVerify ev = repository.saveAndFlush(emailVerity);
			System.out.println(ev.getVerify());
		}
	}
}