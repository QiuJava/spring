package com.example.mapper;

import com.example.entity.Role;
import com.example.mapper.provider.RoleSqlProvider;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

/**
 * 角色数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface RoleMapper {

	@Delete({ "delete from role", "where id = #{id,jdbcType=BIGINT}" })
	int deleteByPrimaryKey(Long id);

	@Insert({ "insert into role (id, role_name, ", "intro, create_time, ", "update_time)",
			"values (#{id,jdbcType=BIGINT}, #{roleName,jdbcType=VARCHAR}, ",
			"#{intro,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ", "#{updateTime,jdbcType=TIMESTAMP})" })
	int insert(Role record);

	@InsertProvider(type = RoleSqlProvider.class, method = "insertSelective")
	int insertSelective(Role record);

	@Select({ "select", "id, role_name, intro, create_time, update_time", "from role",
			"where id = #{id,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "role_name", property = "roleName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP) })
	Role selectByPrimaryKey(Long id);

	@UpdateProvider(type = RoleSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(Role record);

	@Update({ "update role", "set role_name = #{roleName,jdbcType=VARCHAR},", "intro = #{intro,jdbcType=VARCHAR},",
			"create_time = #{createTime,jdbcType=TIMESTAMP},", "update_time = #{updateTime,jdbcType=TIMESTAMP}",
			"where id = #{id,jdbcType=BIGINT}" })
	int updateByPrimaryKey(Role record);
}