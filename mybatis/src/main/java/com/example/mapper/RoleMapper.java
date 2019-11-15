package com.example.mapper;

import com.example.dto.AllotPermissionDto;
import com.example.entity.Role;
import com.example.mapper.provider.RoleSqlProvider;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

/**
 * 角色数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface RoleMapper {

	@Delete({ "DELETE  ", //
			"FROM ", //
			"	role  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int deleteByPrimaryKey(Long id);

	@Insert({ "INSERT INTO role ( id, role_name, intro, create_time, update_time ) ", //
			"VALUES ", //
			"	( #{id,jdbcType=BIGINT} ", //
			"	, #{roleName,jdbcType=VARCHAR} ", //
			"	, #{intro,jdbcType=VARCHAR} ", //
			"	, #{createTime,jdbcType=TIMESTAMP} ", //
			"	, #{updateTime,jdbcType=TIMESTAMP} ", //
			"	)" })
	int insert(Role record);

	@InsertProvider(type = RoleSqlProvider.class, method = "insertSelective")
	int insertSelective(Role record);

	@Select({ "SELECT ", //
			"	id, ", //
			"	role_name, ", //
			"	intro, ", //
			"	create_time, ", //
			"	update_time  ", //
			"FROM ", //
			"	role  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "role_name", property = "roleName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP) })
	Role selectByPrimaryKey(Long id);

	@UpdateProvider(type = RoleSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(Role record);

	@Update({ "UPDATE role  ", //
			"SET role_name = #{roleName,jdbcType=VARCHAR}, ", //
			"intro = #{intro,jdbcType=VARCHAR}, ", //
			"create_time = #{createTime,jdbcType=TIMESTAMP}, ", //
			"update_time = #{updateTime,jdbcType=TIMESTAMP} ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int updateByPrimaryKey(Role record);

	@Select({ "SELECT ", //
			"	role_0.id AS id, ", //
			"	role_0.role_name AS role_name, ", //
			"	role_0.intro AS intro  ", //
			"FROM ", //
			"	role role_0 ", //
			"	JOIN employee_role emplo_role_0 ON role_0.id = emplo_role_0.employee_id  ", //
			"	AND emplo_role_0.employee_id = #{employeeId,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "role_name", property = "roleName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "id", property = "menuList", many = @Many(select = "com.example.mapper.MenuMapper.selectByRoleId", fetchType = FetchType.EAGER)) })
	List<Role> selectByEmployeeId(Long employeeId);

	@Insert({ "INSERT role_permission ( role_id, permission_id ) ", //
			"VALUES ", //
			"	( #{roleId,jdbcType=BIGINT}, #{permissionId,jdbcType=BIGINT} )" })
	int insertRolePermission(AllotPermissionDto allotPermissionDto);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	role_permission  ", //
			"WHERE ", //
			"	role_id = #{roleId,jdbcType=BIGINT}  ", //
			"	AND permission_id = #{permissionId,jdbcType=BIGINT} " })
	long countRolePermissionByRoleIdAndPermissionId(AllotPermissionDto allotPermissionDto);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	role  ", //
			"WHERE ", //
			"	id = #{roleId,jdbcType=BIGINT}  " })
	long countRoleById(Long roleId);

	@Select({ "SELECT ", //
			"	permission_id  ", //
			"FROM ", //
			"	role_permission  ", //
			"WHERE ", //
			"	role_id =1" })
	List<Long> selectEmployeeIdByRoleId(Long roleId);

	@DeleteProvider(type = RoleSqlProvider.class, method = "deleteByPermissionIdList")
	int deleteByPermissionIdList(List<Long> oldPermissionIdList);
}