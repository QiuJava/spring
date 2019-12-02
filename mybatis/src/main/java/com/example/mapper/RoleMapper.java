package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.example.dto.AllotPermissionDto;
import com.example.entity.Role;
import com.example.mapper.provider.RoleSqlProvider;
import com.example.qo.RoleQo;

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
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(Role record);

	@UpdateProvider(type = RoleSqlProvider.class, method = "updateById")
	int updateById(Role record);

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

	@SelectProvider(type = RoleSqlProvider.class, method = "selectByQo")
	@Results({ @Result(column = "id", jdbcType = JdbcType.BIGINT, property = "id", id = true),
			@Result(column = "role_name", jdbcType = JdbcType.VARCHAR, property = "roleName"),
			@Result(column = "intro", jdbcType = JdbcType.VARCHAR, property = "intro") })
	List<Role> selectByQo(RoleQo roleQo);
}