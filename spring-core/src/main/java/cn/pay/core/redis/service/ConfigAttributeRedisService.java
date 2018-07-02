package cn.pay.core.redis.service;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;

import cn.pay.core.redis.RedisService;

/**
 * 用户登录历史Redis服务
 * 
 * @author Administrator
 *
 */
@Service
public class ConfigAttributeRedisService extends RedisService<Collection<ConfigAttribute>> {

	private static final String CONFIG_ATTRIBUTE_KEY = "CONFIG_ATTRIBUTE_KEY";

	@Override
	protected String getRedisKey() {
		return CONFIG_ATTRIBUTE_KEY;
	}

}
