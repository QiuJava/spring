package cn.eeepay.framework.service;

import java.util.Date;
import java.util.List;
public interface RedisService {
	public boolean insertString(String key, Object value) throws Exception;
	public boolean insertString(String key, Object value, Long expireTime) throws Exception;
	public boolean insertString(String key, Object value, Date expireAtTime) throws Exception;
	
	public boolean insertList(String key, Object value) throws Exception;
	public boolean insertList(String key, Object value, Long expireTime) throws Exception;
	public boolean insertList(String key, Object value, Date expireAtTime) throws Exception;
	public boolean insertHash(String key, String sonKey ,Object value) throws Exception;
	public boolean insertHash(String key, String sonKey ,Object value, Long expireTime) throws Exception;
	public boolean insertHash(String key, String sonKey ,Object value, Date expireAtTime) throws Exception;
	
	public boolean insertSet(String key ,Object value) throws Exception;
	public boolean insertSet(String key ,Object value, Long expireTime) throws Exception;
	public boolean insertSet(String key ,Object value, Date expireAtTime) throws Exception;
	//删除名称为key的set中的元素member
	public boolean deleteMemberForSet(String key ,Object... member) throws Exception;
	//member是否是名称为key的set的元素
	public boolean selectIsMemberForSet(String key ,Object member) throws Exception;
	
	public boolean insertZSet(String key ,Object value,Double score) throws Exception;
	public boolean insertZSet(String key ,Object value,Double score, Long expireTime) throws Exception;
	public boolean insertZSet(String key ,Object value,Double score, Date expireAtTime) throws Exception;
	
	public boolean deleteMemberForZSet(String key ,Object... member) throws Exception;
	
	public boolean exists(final String key) throws Exception;
	public Object select(String key) throws Exception;
	public boolean delete(List<String> keys) throws Exception;
}
