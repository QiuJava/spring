package com.example.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.example.entity.LoginLog;
import com.example.mapper.provider.LoginLogSqlProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

/**
 * 登录日志数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface LoginLogMapper {

	@Delete({ "delete from login_log", "where id = #{id,jdbcType=BIGINT}" })
	int deleteByPrimaryKey(Long id);

	@Insert({ "insert into login_log (id, remote_address, ", "username, login_type, ", "remark, create_time, ",
			"update_time)", "values (#{id,jdbcType=BIGINT}, #{remoteAddress,jdbcType=VARCHAR}, ",
			"#{username,jdbcType=VARCHAR}, #{loginType,jdbcType=INTEGER}, ",
			"#{remark,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ", "#{updateTime,jdbcType=TIMESTAMP})" })
	int insert(LoginLog record);

	@InsertProvider(type = LoginLogSqlProvider.class, method = "insertSelective")
	int insertSelective(LoginLog record);

	@Select({ "select", "id, remote_address, username, login_type, remark, create_time, update_time", "from login_log",
			"where id = #{id,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "remote_address", property = "remoteAddress", jdbcType = JdbcType.VARCHAR),
			@Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
			@Result(column = "login_type", property = "loginType", jdbcType = JdbcType.INTEGER),
			@Result(column = "remark", property = "remark", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP) })
	LoginLog selectByPrimaryKey(Long id);

	@UpdateProvider(type = LoginLogSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(LoginLog record);

	@Update({ "update login_log", "set remote_address = #{remoteAddress,jdbcType=VARCHAR},",
			"username = #{username,jdbcType=VARCHAR},", "login_type = #{loginType,jdbcType=INTEGER},",
			"remark = #{remark,jdbcType=VARCHAR},", "create_time = #{createTime,jdbcType=TIMESTAMP},",
			"update_time = #{updateTime,jdbcType=TIMESTAMP}", "where id = #{id,jdbcType=BIGINT}" })
	int updateByPrimaryKey(LoginLog record);

}