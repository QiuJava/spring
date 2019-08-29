package cn.loan.core.config.cache;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * RedisHash服务抽象类
 * 
 * @author Qiujian
 * 
 */
@Configuration
public abstract class AbstractRedisHashService<T> {

	@Autowired
	protected RedisTemplate<String, Object> redisTemplate;

	@Resource
	protected HashOperations<String, String, T> hashOperations;

	/**
	 * 获取存入Redis hash类型的key
	 * 
	 * @return
	 */
	protected abstract String getRedisHashKey();

	/**
	 * 添加键值对 key:Object
	 *
	 * @param key
	 * @param obj
	 * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
	 */
	public void put(String key, T obj, long expire) {
		hashOperations.put(getRedisHashKey(), key, obj);
		if (expire != -1) {
			redisTemplate.expire(getRedisHashKey(), expire, TimeUnit.SECONDS);
		}
	}

	public void remove(String key) {
		hashOperations.delete(getRedisHashKey(), key);
	}

	/**
	 * 查询key名称的元素
	 *
	 * @param key
	 * @return obj
	 */
	public T get(String key) {
		return hashOperations.get(getRedisHashKey(), key);
	}

	public List<T> getAll() {
		return hashOperations.values(getRedisHashKey());
	}

	public Set<String> getKeys() {
		return hashOperations.keys(getRedisHashKey());
	}

	public boolean isKeyExists(String key) {
		return hashOperations.hasKey(getRedisHashKey(), key);
	}

	public long count() {
		return hashOperations.size(getRedisHashKey());
	}

	public void empty() {
		Set<String> set = hashOperations.keys(getRedisHashKey());
		set.forEach(key -> hashOperations.delete(getRedisHashKey(), key));
	}

}