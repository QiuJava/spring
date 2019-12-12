package com.example.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;

import com.example.entity.LoginLog;
import com.example.mapper.provider.LoginLogSqlProvider;

/**
 * 登录日志数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface LoginLogMapper {

	@InsertProvider(type = LoginLogSqlProvider.class, method = "insert")
	int insert(LoginLog record);

	@Select({ "SELECT ", //
			"	create_time  ", //
			"FROM ", //
			"	login_log  ", //
			"WHERE ", //
			"	username = #{username,jdbcType=VARCHAR}  ", //
			"ORDER BY ", //
			"	create_time DESC  ", //
			"	LIMIT 1 " })
	Date selectNewestLoginTimeByUsername(String username);

}