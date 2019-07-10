package cn.loan.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.entity.BankCard;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.repository.BankCardDao;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.UserInfoStatusUtil;

/**
 * 银行卡服务
 * 
 * @author qiujian
 *
 */
@Service
public class BankCardService {

	@Autowired
	private BankCardDao bankCardDao;
	@Autowired
	private UserInfoService userInfoService;

	public BankCard getCurrent() {
		LoginUser currentUser = SecurityContextUtil.getCurrentUser();
		return bankCardDao.findByLoginUserId(currentUser.getId());
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(BankCard bankCard) {
		UserInfo current = userInfoService.getCurrent();
		current.addStatus(UserInfoStatusUtil.OP_BANK_CARD_BIND);
		bankCard.setLoginUserId(current.getId());
		bankCardDao.save(bankCard);
		userInfoService.save(current);
	}

}
