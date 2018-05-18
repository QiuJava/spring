package cn.pay.core.redis.service;

import org.springframework.stereotype.Service;

import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.redis.RedisService;

/**
 * 用户登录历史Redis服务
 * 
 * @author Administrator
 *
 */
@Service
public class IpLogRedisService extends RedisService<IpLog> {

	private static final String REDIS_KEY = "IP_LOG_KEY";

	@Override
	protected String getRedisKey() {
		return REDIS_KEY;
	}

}
