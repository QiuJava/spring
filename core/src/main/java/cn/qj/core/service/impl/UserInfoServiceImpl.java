package cn.qj.core.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.entity.UserInfo;
import cn.qj.core.pojo.vo.VerifyCode;
import cn.qj.core.repository.UserInfoRepository;
import cn.qj.core.service.UserInfoService;
import cn.qj.core.util.BidStateUtil;
import cn.qj.core.util.DateUtil;
import cn.qj.core.util.HttpServletContext;
import cn.qj.core.util.LogicException;

/**
 * 用户信息服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserInfoRepository repository;

	@Override
	public UserInfo get(Long id) {
		return repository.findOne(id);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void saveBasicInfo(UserInfo userInfo) {
		// 拿到当前用户基本资料
		UserInfo info = repository.findOne(HttpServletContext.getCurrentLoginInfo().getId());
		// 设置只需要修改的内容 明细信息
		info.setEducationBackground(userInfo.getEducationBackground());
		info.setHouseCondition(userInfo.getHouseCondition());
		info.setIncomeGrade(userInfo.getIncomeGrade());
		info.setKidCount(userInfo.getKidCount());
		info.setMarriage(userInfo.getMarriage());
		if (!info.getIsBasicInfo()) {
			info.addState(BidStateUtil.OP_BASIC_INFO);
		}
		// 视频认证
		if (!info.getIsVedioAuth()) {
			info.addState(BidStateUtil.OP_VEDIO_AUTH);
		}
		update(info);
	}

	@Override
	@Transactional(rollbackFor = { LogicException.class })
	public void bind(String phoneNumber, String verifyCode) {
		// 如果当前用户已经绑定手机,直接略过
		UserInfo userInfo = get(HttpServletContext.getCurrentLoginInfo().getId());
		if (!userInfo.getIsBindPhone()) {
			// 检查验证码手机号，验证有效期
			boolean ret = checkVerifyCode(phoneNumber, verifyCode);
			if (ret) {
				// 绑定手机 修改状态码
				userInfo.setPhoneNumber(phoneNumber);
				userInfo.addState(BidStateUtil.OP_BIND_PHONE);
				update(userInfo);
			} else {
				throw new LogicException("绑定失败");
			}
		}
	}

	private boolean checkVerifyCode(String phoneNumber, String verifyCode) {
		VerifyCode vc = HttpServletContext.getVerifyCode();
		if (vc != null && vc.getPhoneNumber().equals(phoneNumber) && verifyCode.equals(verifyCode)
				&& DateUtil.setBetweenDate(new Date(), vc.getDate()) < 180) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor = { LogicException.class })
	public void update(UserInfo userInfo) {
		if (repository.saveAndFlush(userInfo) == null) {
			throw new LogicException("用户信息更新乐观锁异常");
		}
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void save(UserInfo userInfo) {
		repository.saveAndFlush(userInfo);
	}

}
