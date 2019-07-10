package cn.loan.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.repository.UserInfoDao;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.UserInfoStatusUtil;

/**
 * 用户信息服务
 * 
 * @author qiujian
 *
 */
@Service
public class UserInfoService {
	
	@Autowired
	private UserInfoDao userInfoDao;

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(UserInfo userInfo) {
		userInfoDao.save(userInfo);
	}

	public UserInfo get(Long id) {
		return userInfoDao.findOne(id);
	}

	public UserInfo getCurrent() {
		LoginUser user = SecurityContextUtil.getCurrentUser();
		return userInfoDao.findOne(user.getId());
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void saveInfo(UserInfo userInfo) {
		UserInfo current = this.getCurrent();
		current.setIncomeGrade(userInfo.getIncomeGrade());
		current.setEducationBackground(userInfo.getEducationBackground());
		if(!current.isBasicInfo()) {
			current.addStatus(UserInfoStatusUtil.OP_BASIC_INFO);
		}
		if(!current.isVedioAuth()) {
			current.addStatus(UserInfoStatusUtil.OP_VEDIO_AUTH);
		}
		this.save(current);
	}

}
