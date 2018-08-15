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
 * 后台应用初始化事件监听 创建Admin登录信息 创建系统账户
 * 
 * @author Qiujian
 * @date 2018年8月13日
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
		LoginInfo info = loginInfoService.getLoginInfoByUsername(SysConst.ADMIN_NAME);
		if (info == null) {
			// 没有创建
			info = new LoginInfo();
			info.setIsAdmin(true);
			info.setPassword(new BCryptPasswordEncoder().encode(SysConst.LOGIN_PASSWORD));
			info.setUsername(SysConst.ADMIN_NAME);
			info.setUserType(LoginInfo.MANAGER);
			Date currentDate = new Date();
			info.setGmtCreate(currentDate);
			info.setGmtModified(currentDate);
			loginInfoService.saveLoginInfo(info);
		}

		Long accountCount = systemAccountService.count();
		if (accountCount < 1) {
			// 创建系统账户
			SystemAccount sysAcc = new SystemAccount();
			Date currentDate = new Date();
			sysAcc.setCreateDate(currentDate);
			sysAcc.setTotalBalance(BigDecimal.ZERO);
			sysAcc.setFreezedAmount(BigDecimal.ZERO);
			sysAcc.setBeginDate(currentDate);
			sysAcc.setEndDate(DateUtils.addYears(currentDate, 1));
			systemAccountService.save(sysAcc);
		}
	}

}
