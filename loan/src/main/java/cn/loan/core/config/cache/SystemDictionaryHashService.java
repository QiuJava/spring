package cn.loan.core.config.cache;

import org.springframework.context.annotation.Configuration;

import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.util.StringUtil;

/**
 * 系统字典Hash服务
 * 
 * @author qiujian
 *
 */
@Configuration
public class SystemDictionaryHashService extends AbstractRedisHashService<SystemDictionary> {

	@Override
	protected String getRedisHashKey() {
		return StringUtil.SYSTEM_DICTIONARY_HASH;
	}

}
