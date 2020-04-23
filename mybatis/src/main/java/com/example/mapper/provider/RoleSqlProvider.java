package com.example.mapper.provider;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.example.qo.RoleQo;
import com.example.util.SqlUtil;
import com.example.util.StrUtil;

/**
 * 角色sql供应
 * 
 * @author Qiu Jian
 *
 */
public class RoleSqlProvider {

	public String deleteRolePermissionByPermissionIdList(Map<String, List<Long>> params) {
		SQL sql = new SQL();
		sql.DELETE_FROM("role_permission");
		sql.WHERE(SqlUtil.inJoint("permission_id", "list", params.get("list")));
		return sql.toString();
	}

	public String selectByQo(RoleQo roleQo) {
		SQL sql = new SQL();
		sql.SELECT("id", "role_name");
		sql.FROM("role");
		if (StrUtil.hasText(roleQo.getRoleName())) {
			sql.WHERE("role_name=#{roleName,jdbcType=VARCHAR}");
		}
		return sql.toString();
	}
}