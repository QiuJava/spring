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

	public String insertSelective(LoginLog record) {
		SQL sql = new SQL();
		sql.INSERT_INTO("login_log");

		if (record.getId() != null) {
			sql.VALUES("id", "#{id,jdbcType=BIGINT}");
		}

		if (record.getRemoteAddress() != null) {
			sql.VALUES("remote_address", "#{remoteAddress,jdbcType=VARCHAR}");
		}

		if (record.getUsername() != null) {
			sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
		}

		if (record.getLoginType() != null) {
			sql.VALUES("login_type", "#{loginType,jdbcType=INTEGER}");
		}

		if (record.getRemark() != null) {
			sql.VALUES("remark", "#{remark,jdbcType=VARCHAR}");
		}

		if (record.getCreateTime() != null) {
			sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
		}

		if (record.getUpdateTime() != null) {
			sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
		}

		return sql.toString();
	}

}