package cn.pay.core.redis.service;

import org.springframework.stereotype.Service;

import cn.pay.core.domain.business.BorrowAuditHistroy;
import cn.pay.core.redis.RedisService;

@Service
public class BorrowAuditHistroyRedisService extends RedisService<BorrowAuditHistroy> {

	private static final String REDIS_KEY = "BORROW_AUDIT_HISTROY_KEY";

	@Override
	protected String getRedisKey() {
		return REDIS_KEY;
	}

}
