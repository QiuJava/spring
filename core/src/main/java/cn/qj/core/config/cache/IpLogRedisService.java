package cn.qj.core.config.cache;

import org.springframework.stereotype.Service;

import cn.qj.core.consts.RedisKeyConst;
import cn.qj.core.entity.IpLog;

/**
 * 登录历史缓存服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Service
public class IpLogRedisService extends AbstractRedisService<IpLog> {

	@Override
	protected String getRedisKey() {
		return RedisKeyConst.IP_LOG_KEY;
	}

}
