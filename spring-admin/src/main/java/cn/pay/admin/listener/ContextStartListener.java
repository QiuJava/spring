package cn.pay.admin.listener;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import cn.pay.core.consts.SysConst;
import cn.pay.core.domain.business.SystemAccount;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.service.LoginInfoService;
import cn.pay.core.service.SystemAccountService;

/**
 * 后台应用初始化事件监听
 * 创建Admin登录信息
 * 创建系统账户
 * 
 * @author Qiujian
 *
 */
@Component
public class ContextStartListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private LoginInfoService loginInfoService;
	@Autowired
	private SystemAccountService systemAccountService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 检查是否有admin
		LoginInfo info = loginInfoService.getByUsername(SysConst.ADMIN_NAME);
		if (info == null) {
			// 没有创建
			info = new LoginInfo();
			info.setAdmin(true);
			info.setPassword(new BCryptPasswordEncoder().encode(SysConst.LOGIN_PASSWORD));
			info.setUsername(SysConst.ADMIN_NAME);
			info.setUserType(LoginInfo.MANAGER);
			loginInfoService.saveAndUpdate(info);
		}

		Long count = systemAccountService.count();
		if (count < 1) {
			// 创建系统账户
			SystemAccount systemAccount = new SystemAccount();
			Date date = new Date();
			systemAccount.setCreateDate(date);
			systemAccount.setTotalBalance(BigDecimal.ZERO);
			systemAccount.setFreezedAmount(BigDecimal.ZERO);
			systemAccount.setBeginDate(date);
			systemAccount.setEndDate(DateUtils.addYears(date, 1));
			systemAccountService.save(systemAccount);
		}
	}

}
