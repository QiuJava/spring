package cn.eeepay.framework.service.bill.impl;


import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.service.bill.RedisService;

@Service("redisService")
@Transactional
public class RedisServiceImpl implements RedisService{
	@Resource
	protected RedisTemplate<String, Object> redisTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);
	
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	@Override
	public boolean insertString(String key, Object value) {
		try {
			 redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			log.info("Redis新增String错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertString(String key, Object value, Long expireTime) throws Exception {
		try {
			 redisTemplate.opsForValue().set(key, value);
			 redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.info("Redis新增String错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertString(String key, Object value, Date expireAtTime) throws Exception {
		try {
			 redisTemplate.opsForValue().set(key, value);
			 redisTemplate.expireAt(key, expireAtTime);
			return true;
		} catch (Exception e) {
			log.info("Redis新增String错误:{}", e.getMessage());
			return false;
		}
	}
	
	@Override
	public boolean insertList(String key, Object value) {
		try {
			redisTemplate.opsForList().leftPushAll(key, value);	
			return true;
		} catch (Exception e) {
			log.info("Redis新增List错误:{}", e.getMessage());
			return false;
		}

	}
	@Override
	public boolean insertList(String key, Object value, Long expireTime) throws Exception {
		try {
			redisTemplate.opsForList().leftPushAll(key, value);	
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.info("Redis新增List错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertList(String key, Object value, Date expireAtTime) throws Exception {
		try {
			redisTemplate.opsForList().leftPushAll(key, value);	
			redisTemplate.expireAt(key, expireAtTime);
			return true;
		} catch (Exception e) {
			log.info("Redis新增List错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertHash(String key, String sonKey ,Object value) {
		try {
			redisTemplate.opsForHash().put(key, sonKey, value);	
			return true;
		} catch (Exception e) {
			log.info("Redis新增Hash错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertHash(String key, String sonKey, Object value, Long expireTime) throws Exception {
		try {
			redisTemplate.opsForHash().put(key, sonKey, value);	
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.info("Redis新增Hash错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertHash(String key, String sonKey, Object value, Date expireAtTime) throws Exception {
		try {
			redisTemplate.opsForHash().put(key, sonKey, value);	
			redisTemplate.expireAt(key, expireAtTime);
			return true;
		} catch (Exception e) {
			log.info("Redis新增Hash错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean deleteSonkeyForHash(String key, Object... sonKeys) throws Exception {
		try {
			redisTemplate.opsForHash().delete(key, sonKeys);;	
			return true;
		} catch (Exception e) {
			log.info("Redis删除Hash Sonkey错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}
	@Override
	public Object select(String key){
		try {
			 DataType type = redisTemplate.type(key);
			 if(DataType.NONE == type){
				 log.info("Redis key不存在");
				 return null;
			 }else if(DataType.STRING == type){
				 return redisTemplate.opsForValue().get(key);
			 }else if(DataType.LIST == type){
				 return redisTemplate.opsForList().range(key, 0, -1);
			 }else if(DataType.HASH == type){
				 return redisTemplate.opsForHash().entries(key);
			 }else if(DataType.SET == type){
				 return redisTemplate.opsForSet().members(key);
			 }else if(DataType.ZSET == type){
				 return redisTemplate.opsForZSet().range(key, 0, -1);
			 }else
				 return null;
		} catch (Exception e) {
			log.info("Redis查询错误:{}", e.getMessage());
			return null;
		}
	}
	@Override
	public Set<String> keys(String pattern) throws Exception {
		return redisTemplate.keys(pattern);
	}
	@Override
	public boolean delete(String key) throws Exception {
		try{
			redisTemplate.delete(key);
			return true;
		}catch(Exception e){
			log.info("Redis 删除key失败:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean delete(List<String> keys){
		try{
			redisTemplate.delete(keys);
			return true;
		}catch(Exception e){
			log.info("Redis 删除 List keys失败:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertSet(String key, Object value) throws Exception {
		try {
			redisTemplate.opsForSet().add(key, value);
			return true;
		} catch (Exception e) {
			log.info("Redis Set新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertSet(String key, Object value, Long expireTime) throws Exception {
		try {
			redisTemplate.opsForSet().add(key, value);	
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.info("Redis Set新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertSet(String key, Object value, Date expireAtTime) throws Exception {
		try {
			redisTemplate.opsForSet().add(key, value);	
			redisTemplate.expireAt(key, expireAtTime);
			return true;
		} catch (Exception e) {
			log.info("Redis Set新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean deleteMemberForSet(String key, Object... member) throws Exception {
		try {
			redisTemplate.opsForSet().remove(key, member);
			return true;
		} catch (Exception e) {
			log.info("Redis Set 删除错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean selectIsMemberForSet(String key, Object member) throws Exception {
		try {
			return redisTemplate.opsForSet().isMember(key, member);
		} catch (Exception e) {
			log.info("Redis Set IsMember错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertZSet(String key, Object value, Double score) throws Exception {
		try {
			redisTemplate.opsForZSet().add(key, value , score);
			return true;
		} catch (Exception e) {
			log.info("Redis新增ZSet错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertZSet(String key, Object value, Double score, Long expireTime) throws Exception {
		try {
			redisTemplate.opsForZSet().add(key, value , score);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.info("Redis新增ZSet错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertZSet(String key, Object value, Double score, Date expireAtTime) throws Exception {
		try {
			redisTemplate.opsForZSet().add(key, value , score);
			redisTemplate.expireAt(key, expireAtTime);
			return true;
		} catch (Exception e) {
			log.info("Redis新增ZSet错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean deleteMemberForZSet(String key, Object... member) throws Exception {
		try {
			redisTemplate.opsForZSet().remove(key, member);
			return true;
		} catch (Exception e) {
			log.info("Redis ZSet 删除错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean hasKeyForHash(String key, Object sonKey) throws Exception {
		try {
			return redisTemplate.opsForHash().hasKey(key, sonKey);	
		} catch (Exception e) {
			log.info("Redis查找Hash key错误:{}", e.getMessage());
			return false;
		}
	}

	

}
