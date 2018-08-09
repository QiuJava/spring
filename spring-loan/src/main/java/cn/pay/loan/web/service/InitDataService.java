package cn.pay.loan.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.service.IpLogService;
import cn.pay.core.service.LoginInfoService;
import cn.pay.loan.config.ActiveMQConfig;

@Service
public class InitDataService {
	@Autowired
	private LoginInfoService loginInfoService;
	@Autowired
	private IpLogService ipLogService;

	@JmsListener(destination = ActiveMQConfig.LOGIN_INFO_QUEUE)
	public void loginInfo(String msg) {
		loginInfoService.saveAndUpdate(JSON.parseObject(msg, LoginInfo.class));
	}

	@JmsListener(destination = ActiveMQConfig.IP_LOG_QUEUE)
	public void ipLog(String msg) {
		ipLogService.saveAndUpdate(JSON.parseObject(msg, IpLog.class));
	}
}
