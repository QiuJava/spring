package cn.qj.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.entity.UserBankInfo;
import cn.qj.core.entity.UserInfo;
import cn.qj.core.repository.UserBankInfoRepository;
import cn.qj.core.service.UserBankInfoService;
import cn.qj.core.service.UserInfoService;
import cn.qj.core.util.BidStateUtil;
import cn.qj.core.util.HttpServletContext;

/**
 * 用户银行卡信息服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
@Service
public class UserBankInfoServiceImpl implements UserBankInfoService {

	@Autowired
	private UserBankInfoRepository repository;
	@Autowired
	private UserInfoService userInfoService;

	@Override
	public UserBankInfo getByLoginInfoId(Long id) {
		return repository.findByLoginInfoId(id);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void save(UserBankInfo userBankInfo) {
		// 得到用户信息对象
		UserInfo info = userInfoService.get(HttpServletContext.getCurrentLoginInfo().getId());
		if (!info.getIsBankBind() && info.getIsRealAuth()) {
			// 没绑定 进行绑定 保存用户银行卡信息 修改状态
			userBankInfo.setAccountName(info.getRealName());
			userBankInfo.setLoginInfoId(info.getId());
			info.addState(BidStateUtil.OP_USERBANKINFO_BIND);
			repository.saveAndFlush(userBankInfo);
			userInfoService.update(info);
		}
	}

}
