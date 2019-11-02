package com.example.cache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.config.context.SpringContextHelper;

/**
 * Mybatis二级缓存
 *
 * @author Qiu Jian
 *
 */
public class MybatisSecondCache implements Cache {

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

	private RedisTemplate<String, Object> redisTemplate;

	private String id;

	public MybatisSecondCache(final String id) {
		if (id == null) {
			throw new IllegalArgumentException("缓存实例id不能为空");
		}
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void putObject(Object key, Object value) {
		this.setRedisTemplate();
		redisTemplate.opsForValue().set(key.toString(), value, 2, TimeUnit.MINUTES);
	}

	@Override
	public Object getObject(Object key) {
		this.setRedisTemplate();
		Object obj = redisTemplate.opsForValue().get(key.toString());
		return obj;
	}

	@Override
	public Object removeObject(Object key) {
		this.setRedisTemplate();
		return redisTemplate.delete(key.toString());
	}

	@Override
	public void clear() {
		this.setRedisTemplate();
		redisTemplate.execute((RedisCallback<Long>) connection -> {
			connection.flushDb();
			return null;
		});
	}

	@Override
	public int getSize() {
		this.setRedisTemplate();
		Long size = redisTemplate.execute((RedisCallback<Long>) connection -> connection.dbSize());
		return size.intValue();
	}

	@Override
	public ReadWriteLock getReadWriteLock() {
		return this.readWriteLock;
	}

	private void setRedisTemplate() {
		if (redisTemplate == null) {
			redisTemplate = SpringContextHelper.getBean("redisTemplate");
		}
	}
}
