package cn.loan.core.config.eventlistener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.SystemAccount;
import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.service.LoginUserService;
import cn.loan.core.service.SystemAccountService;
import cn.loan.core.service.SystemDictionaryService;
import cn.loan.core.util.BigDecimalUtil;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 应用启动或刷新监听处理逻辑
 * 
 * @author qiujian
 *
 */
@Configuration
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private LoginUserService loginUserService;
	@Autowired
	private SystemDictionaryService systemDictionaryService;
	@Autowired
	private SystemAccountService systemAccountService;

	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		systemDictionaryService.updateHash();
		SystemDictionary dict = systemDictionaryHashService.get(SystemDictionaryUtil.USER_TYPE);
		Integer userType = Integer
				.valueOf(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_TYPE_MANAGE, dict));
		// 创建系统用户
		boolean exist = loginUserService.existUserByUserType(Integer.valueOf(userType));
		if (!exist) {
			LoginUser loginUser = new LoginUser();
			SystemDictionary admin = systemDictionaryHashService.get(SystemDictionaryUtil.INIT);
			String itemValue = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.INIT_USERNAME, admin);
			String initPassword = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.INIT_PASSWORD, admin);

			SystemDictionary normal = systemDictionaryHashService.get(SystemDictionaryUtil.USER_STATUS);
			loginUser.setUsername(itemValue);
			loginUser.setPassword(bCryptPasswordEncoder.encode(initPassword));
			Integer userStatus = Integer
					.valueOf(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.USER_STATUS_NORMAL, normal));
			loginUser.setUserStatus(userStatus);
			loginUser.setUserType(userType);
			loginUser.setLockingTime(DateUtil.getNewDate());
			loginUser.setLoginFailureCount(0);
			
			loginUserService.save(loginUser);
		}

		exist = systemAccountService.existAccount();
		if (!exist) {
			SystemAccount systemAccount = new SystemAccount();
			systemAccount.setFreezedAmount(BigDecimalUtil.ZERO);
			systemAccount.setUsableBalance(BigDecimalUtil.ZERO);
			systemAccountService.save(systemAccount);
		}
	}

}
