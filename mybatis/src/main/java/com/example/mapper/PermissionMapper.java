package com.example.mapper;

import com.example.entity.Permission;
import com.example.mapper.provider.PermissionSqlProvider;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface PermissionMapper {
	@Delete({ "delete from permission", "where id = #{id,jdbcType=BIGINT}" })
	int deleteByPrimaryKey(Long id);

	@Insert({ "insert into permission (id, permission_name, ", "authority, intro, ", "create_time, update_time, ",
			"menu_id)", "values (#{id,jdbcType=BIGINT}, #{permissionName,jdbcType=VARCHAR}, ",
			"#{authority,jdbcType=VARCHAR}, #{intro,jdbcType=VARCHAR}, ",
			"#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP}, ", "#{menuId,jdbcType=BIGINT})" })
	int insert(Permission record);

	@InsertProvider(type = PermissionSqlProvider.class, method = "insertSelective")
	int insertSelective(Permission record);

	@Select({ "select", "id, permission_name, authority, intro, create_time, update_time, menu_id", "from permission",
			"where id = #{id,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "permission_name", property = "permissionName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "authority", property = "authority", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "menu_id", property = "menuId", jdbcType = JdbcType.BIGINT) })
	Permission selectByPrimaryKey(Long id);

	@UpdateProvider(type = PermissionSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(Permission record);

	@Update({ "update permission", "set permission_name = #{permissionName,jdbcType=VARCHAR},",
			"authority = #{authority,jdbcType=VARCHAR},", "intro = #{intro,jdbcType=VARCHAR},",
			"create_time = #{createTime,jdbcType=TIMESTAMP},", "update_time = #{updateTime,jdbcType=TIMESTAMP},",
			"menu_id = #{menuId,jdbcType=BIGINT}", "where id = #{id,jdbcType=BIGINT}" })
	int updateByPrimaryKey(Permission record);

	@Select({"SELECT ", 
			"	permi_O.id AS id, ", 
			"	permi_O.permission_name AS permission_name, ", 
			"	permi_O.authority AS authority, ", 
			"	permi_O.intro AS intro, ", 
			"	permi_O.menu_id AS menu_id  ", 
			"FROM ", 
			"	permission permi_O ", 
			"	JOIN role_permission role_permi_0 ON permi_O.id = role_permi_0.permission_id ", 
			"	JOIN employee_role emplo_role_0 ON role_permi_0.role_id  ", 
			"	AND emplo_role_0.employee_id = #{employeeId,jdbcType=BIGINT}"})
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
		@Result(column = "permission_name", property = "permissionName", jdbcType = JdbcType.VARCHAR),
		@Result(column = "authority", property = "authority", jdbcType = JdbcType.VARCHAR),
		@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
		@Result(column = "menu_id", property = "menuId", jdbcType = JdbcType.BIGINT) })
	List<Permission> selectByEmployeeId(long employeeId);
}