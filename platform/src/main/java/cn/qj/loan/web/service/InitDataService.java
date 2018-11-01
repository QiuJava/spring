package cn.qj.loan.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.qj.core.entity.IpLog;
import cn.qj.core.entity.LoginInfo;
import cn.qj.core.service.IpLogService;
import cn.qj.core.service.LoginInfoService;
import cn.qj.loan.config.mq.ActivemqConfig;

/**
 * 初始化数据服务
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
@Service
public class InitDataService {
	@Autowired
	private LoginInfoService loginInfoService;
	@Autowired
	private IpLogService ipLogService;

	@JmsListener(destination = ActivemqConfig.LOGIN_INFO_QUEUE)
	public void loginInfo(String msg) {
		loginInfoService.updateLoginInfo(JSON.parseObject(msg, LoginInfo.class));
	}

	@JmsListener(destination = ActivemqConfig.IP_LOG_QUEUE)
	public void ipLog(String msg) {
		ipLogService.saveIpLog(JSON.parseObject(msg, IpLog.class));
	}
}
