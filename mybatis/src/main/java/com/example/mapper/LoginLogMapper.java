package com.example.mapper;

import org.apache.ibatis.annotations.InsertProvider;

import com.example.entity.LoginLog;
import com.example.mapper.provider.LoginLogSqlProvider;

/**
 * 登录日志数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface LoginLogMapper {

	@InsertProvider(type = LoginLogSqlProvider.class, method = "insertSelective")
	int insertSelective(LoginLog record);

}