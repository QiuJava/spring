package cn.qj.key.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 短信服务
 * 
 * @author Qiujian
 * @date 2019年3月8日
 *
 */
@Service
public class SmsService {

	private static final Logger log = LoggerFactory.getLogger(SmsService.class);

	public void send(String phoneNum, String authCode) {
		// 发送短信到短信网关
		log.info("发送成功。验证码：" + authCode);
	}

}
