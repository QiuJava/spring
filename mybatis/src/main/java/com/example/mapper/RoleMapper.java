package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import com.example.dto.AllotPermissionDto;
import com.example.dto.InitPermissionDto;
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

	int deleteByPrimaryKey(Integer id);

	int insert(Role record);

	int insertSelective(Role record);

	Role selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Role record);

	int updateByPrimaryKey(Role record);

	@Delete({ "DELETE  ", //
			"FROM ", //
			"	role  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int deleteById(Long id);

	@Insert({ "INSERT role_permission ( role_id, permission_id ) ", //
			"VALUES ", //
			"	( #{roleId,jdbcType=BIGINT}, #{permissionId,jdbcType=BIGINT} )" })
	int insertRolePermission(AllotPermissionDto allotPermissionDto);

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

	@Delete({ "DELETE ", //
			"FROM ", //
			"	role_permission  ", //
			"WHERE ", //
			"	role_id = #{roleId,jdbcType=BIGINT}  " })
	int deleteRolePermissionByRoleId(Long roleId);

	@SelectProvider(type = RoleSqlProvider.class, method = "selectByQo")
	@Results({ @Result(column = "id", jdbcType = JdbcType.BIGINT, property = "id", id = true),
			@Result(column = "role_name", jdbcType = JdbcType.VARCHAR, property = "roleName") })
	List<Role> selectByQo(RoleQo roleQo);

	@Delete({ "DELETE  ", //
			"FROM ", //
			"	role_permission  ", //
			"WHERE ", //
			"	role_id = #{roleId,jdbcType=BIGINT}  ", //
			"	AND permission_id IN ( SELECT id FROM permission WHERE menu_id = #{menuId,jdbcType=BIGINT} )" })
	int deleteRolePermissionByRoleIdAndMenuId(InitPermissionDto initPermissionDto);
}