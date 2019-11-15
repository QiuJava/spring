package com.example.mapper.provider;

import com.example.entity.Role;
import com.example.util.SqlUtil;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

/**
 * 角色sql供应
 * 
 * @author Qiu Jian
 *
 */
public class RoleSqlProvider {

	public String insertSelective(Role record) {
		SQL sql = new SQL();
		sql.INSERT_INTO("role");

		if (record.getId() != null) {
			sql.VALUES("id", "#{id,jdbcType=BIGINT}");
		}

		if (record.getRoleName() != null) {
			sql.VALUES("role_name", "#{roleName,jdbcType=VARCHAR}");
		}

		if (record.getIntro() != null) {
			sql.VALUES("intro", "#{intro,jdbcType=VARCHAR}");
		}

		if (record.getCreateTime() != null) {
			sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
		}

		if (record.getUpdateTime() != null) {
			sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
		}

		return sql.toString();
	}

	public String updateByPrimaryKeySelective(Role record) {
		SQL sql = new SQL();
		sql.UPDATE("role");

		if (record.getRoleName() != null) {
			sql.SET("role_name = #{roleName,jdbcType=VARCHAR}");
		}

		if (record.getIntro() != null) {
			sql.SET("intro = #{intro,jdbcType=VARCHAR}");
		}

		if (record.getCreateTime() != null) {
			sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
		}

		if (record.getUpdateTime() != null) {
			sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
		}

		sql.WHERE("id = #{id,jdbcType=BIGINT}");

		return sql.toString();
	}

	public String deleteByPermissionIdList(Map<String, List<Long>> params) {
		SQL sql = new SQL();
		sql.DELETE_FROM("role_permission");
		sql.WHERE(SqlUtil.inJoint("permission_id", "list", params.get("list")));
		return sql.toString();
	}
}