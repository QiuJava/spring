package cn.pay.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.UserBankInfoRepository;
import cn.pay.core.domain.business.UserBankInfo;
import cn.pay.core.domain.business.UserInfo;
import cn.pay.core.service.UserBankInfoService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.BidStateUtil;
import cn.pay.core.util.HttpServletContext;

/**
 * 用户银行卡信息服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
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
