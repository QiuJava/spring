package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.example.entity.Permission;
import com.example.mapper.provider.PermissionSqlProvider;
import com.example.qo.PermissionQo;
import com.example.vo.PermissionCheckboxVo;

/**
 * 权限数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface PermissionMapper {
	@Delete({ "DELETE  ", //
			"FROM ", //
			"	permission  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int deleteById(Long id);

	@Select({ "SELECT ", //
			"	permission_name, ", //
			"	authority, ", //
			"	url ", //
			"FROM ", //
			"	permission  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	@Results({ @Result(column = "permission_name", property = "permissionName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "authority", property = "authority", jdbcType = JdbcType.VARCHAR),
			@Result(column = "url", property = "url", jdbcType = JdbcType.VARCHAR) })
	Permission selectById(Long id);

	@Select({ "SELECT DISTINCT ", //
			"	permi_O.id AS id, ", //
			"	permi_O.menu_id AS menu_id, ", //
			"	permi_O.authority AS authority, ", //
			"	permi_O.permission_name AS permission_name, ", //
			"	permi_O.intro AS intro, ", //
			"	permi_O.url AS url ", //
			"FROM ", //
			"	permission permi_O ", //
			"	JOIN role_permission role_permi_0 ON permi_O.id = role_permi_0.permission_id ", //
			"	JOIN employee_role emplo_role_0 ON role_permi_0.role_id  ", //
			"	AND emplo_role_0.employee_id = #{employeeId,jdbcType=BIGINT}" })
	@Results(value = { @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "menu_id", property = "menuId", jdbcType = JdbcType.BIGINT),
			@Result(column = "authority", property = "authority", jdbcType = JdbcType.VARCHAR),
			@Result(column = "permission_name", property = "permissionName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "url", property = "url", jdbcType = JdbcType.VARCHAR) })
	List<Permission> selectByEmployeeId(Long employeeId);

	@Select({ "SELECT ", //
			"	id, ", //
			"	permission_name ", //
			"FROM ", //
			"	permission ", //
			"WHERE ", //
			"	menu_id = #{menuId,jdbcType=BIGINT}" })
	@Results(value = { @Result(column = "id", property = "permissionId", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "permission_name", property = "permissionName", jdbcType = JdbcType.VARCHAR) })
	List<PermissionCheckboxVo> selectPermissionCheckboxVoByMenuId(Long menuId);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	permission  ", //
			"WHERE ", //
			"	permission_name = #{permissionName,jdbcType=VARCHAR}" })
	int countByPermissionName(String permissionName);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	permission  ", //
			"WHERE ", //
			"	authority = #{authority,jdbcType=VARCHAR}" })
	int countByAuthority(String authority);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	permission  ", //
			"WHERE ", //
			"	url = #{url,jdbcType=VARCHAR}" })
	int countByUrl(String url);

	@Delete({ "DELETE  ", //
			"FROM ", //
			"	role_permission  ", //
			"WHERE ", //
			"	permission_id = #{id,jdbcType=BIGINT}" })
	int deleteRolePermissionByPermissionId(Long id);

	@SelectProvider(type = PermissionSqlProvider.class, method = "selectByQo")
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "permission_name", property = "permissionName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "authority", property = "authority", jdbcType = JdbcType.VARCHAR),
			@Result(column = "url", property = "url", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR) })
	List<Permission> selectByQo(PermissionQo qo);

	@Delete({ "DELETE  ", //
			"FROM ", //
			"	permission  ", //
			"WHERE ", //
			"	menu_id = #{menuId,jdbcType=BIGINT}" })
	int deleteByMenuId(Long menuId);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	permission  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	long countById(Long id);

	@InsertProvider(type = PermissionSqlProvider.class, method = "insert")
	int insert(Permission permission);

	@UpdateProvider(type = PermissionSqlProvider.class, method = "updateById")
	int updateById(Permission permission);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	`role_permission`  ", //
			"WHERE ", //
			"	permission_id = #{id,jdbcType=BIGINT}" })
	long countRolePermissionByPermissionId(Long id);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	permission  ", //
			"WHERE ", //
			"	menu_id = #{menuId,jdbcType=BIGINT}" })
	long countByMenuId(Long menuId);

	@Select({ "SELECT ", //
		"	permission_id  ", //
		"FROM ", //
		"	`role_permission`  ", //
		"WHERE ", //
		"	role_id = #{roleId,jdbcType=BIGINT}" })
	List<Long> selectIdByRoleId(Long roleId);
}