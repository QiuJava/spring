package com.example.mapper.provider;

import com.example.entity.LoginLog;
import org.apache.ibatis.jdbc.SQL;

/**
 * 登录日志Sql供应
 * 
 * @author Qiu Jian
 *
 */
public class LoginLogSqlProvider {

	public String insert(LoginLog record) {
		SQL sql = new SQL();
		sql.INSERT_INTO("login_log");
		sql.VALUES("remote_address", "#{remoteAddress,jdbcType=VARCHAR}");
		sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
		sql.VALUES("login_type", "#{loginType,jdbcType=INTEGER}");
		if (record.getRemark() != null) {
			sql.VALUES("remark", "#{remark,jdbcType=VARCHAR}");
		}
		sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
		sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");

		return sql.toString();
	}

}