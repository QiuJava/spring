package cn.pay.core.redis.service;

import org.springframework.stereotype.Service;

import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.redis.AbstractRedisService;

/**
 * 用户登录历史Redis服务
 * 
 * @author Administrator
 *
 */
@Service
public class IpLogRedisService extends AbstractRedisService<IpLog> {

	private static final String IP_LOG_KEY = "IP_LOG_KEY";

	@Override
	protected String getRedisKey() {
		return IP_LOG_KEY;
	}

}
