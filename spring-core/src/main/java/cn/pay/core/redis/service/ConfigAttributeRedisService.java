package cn.pay.core.redis.service;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;

import cn.pay.core.redis.AbstractRedisService;

/**
 * 系统权限信息
 * 
 * @author Administrator
 *
 */
@Service
public class ConfigAttributeRedisService extends AbstractRedisService<Collection<ConfigAttribute>> {

	private static final String CONFIG_ATTRIBUTE_KEY = "CONFIG_ATTRIBUTE_KEY";

	@Override
	protected String getRedisKey() {
		return CONFIG_ATTRIBUTE_KEY;
	}

}
