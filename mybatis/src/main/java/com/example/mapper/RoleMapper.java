package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import com.example.dto.AllotPermissionDto;
import com.example.entity.Role;
import com.example.mapper.provider.RoleSqlProvider;

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
	int deleteById(Long id);

	@InsertProvider(type = RoleSqlProvider.class, method = "insert")
	int insert(Role record);

	@UpdateProvider(type = RoleSqlProvider.class, method = "updateById")
	int updateById(Role record);

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
	long countById(Long roleId);

	@Select({ "SELECT ", //
			"	permission_id  ", //
			"FROM ", //
			"	role_permission  ", //
			"WHERE ", //
			"	role_id =1" })
	List<Long> selectPermissionIdByRoleId(Long roleId);

	@DeleteProvider(type = RoleSqlProvider.class, method = "deleteRolePermissionByPermissionIdList")
	int deleteRolePermissionByPermissionIdList(List<Long> oldPermissionIdList);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	role  ", //
			"WHERE ", //
			"	role_name = #{roleName,jdbcType=VARCHAR}  " })
	int countByRoleName(String roleName);

	@Select({ "SELECT ", //
			"	role_name  ", //
			"FROM ", //
			"	role  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}  " })
	String selectRoleNameById(Long id);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	role_permission  ", //
			"WHERE ", //
			"	role_id = #{roleId,jdbcType=BIGINT}  " })
	int countRolePermissionByRoleId(Long roleId);

	@Delete({ "DELETE ", //
			"FROM ", //
			"	role_permission  ", //
			"WHERE ", //
			"	role_id = #{roleId,jdbcType=BIGINT}  " })
	int deleteRolePermissionByRoleId(Long roleId);
}