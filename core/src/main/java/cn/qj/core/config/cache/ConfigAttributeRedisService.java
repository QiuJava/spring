package cn.qj.core.config.cache;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;

import cn.qj.core.consts.RedisKeyConst;

/**
 * 权限配置属性缓存服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Service
public class ConfigAttributeRedisService extends AbstractRedisService<Collection<ConfigAttribute>> {

	@Override
	protected String getRedisKey() {
		return RedisKeyConst.CONFIG_ATTRIBUTE_KEY;
	}

}
