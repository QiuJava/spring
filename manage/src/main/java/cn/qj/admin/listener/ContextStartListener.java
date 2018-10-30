package cn.qj.admin.listener;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import cn.qj.core.consts.SysConst;
import cn.qj.core.entity.LoginInfo;
import cn.qj.core.entity.SystemAccount;
import cn.qj.core.service.LoginInfoService;
import cn.qj.core.service.SystemAccountService;

/**
 * 应用初始化监听
 * 
 * @author Qiujian
 * @date 2018/10/30
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
		Boolean isAdmin = loginInfoService.isExistAdmin(true);
		// 没有创建
		if (!isAdmin) {
			LoginInfo info = new LoginInfo();
			info.setIsAdmin(true);
			info.setPassword(new BCryptPasswordEncoder().encode(SysConst.INIT_PASSWORD));
			info.setUsername("超级管理员");
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
