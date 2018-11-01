package cn.qj.core.config.cache;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;

/**
 * 权限配置缓存
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Service
public class ConfigAttributeRedisService extends AbstractRedisService<Collection<ConfigAttribute>> {

	private static final String CONFIG_ATTRIBUTE_KEY = "CONFIG_ATTRIBUTE_KEY";

	@Override
	protected String getRedisKey() {
		return CONFIG_ATTRIBUTE_KEY;
	}

}
