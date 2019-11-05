package com.example.mapper.provider;

import com.example.entity.Permission;
import org.apache.ibatis.jdbc.SQL;

public class PermissionSqlProvider {

	public String insertSelective(Permission record) {
		SQL sql = new SQL();
		sql.INSERT_INTO("permission");
		if (record.getId() != null) {
			sql.VALUES("id", "#{id,jdbcType=BIGINT}");
		}
		if (record.getPermissionName() != null) {
			sql.VALUES("permission_name", "#{permissionName,jdbcType=VARCHAR}");
		}
		if (record.getAuthority() != null) {
			sql.VALUES("authority", "#{authority,jdbcType=VARCHAR}");
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
		if (record.getMenuId() != null) {
			sql.VALUES("menu_id", "#{menuId,jdbcType=BIGINT}");
		}
		return sql.toString();
	}

	public String updateByPrimaryKeySelective(Permission record) {
		SQL sql = new SQL();
		sql.UPDATE("permission");
		if (record.getPermissionName() != null) {
			sql.SET("permission_name = #{permissionName,jdbcType=VARCHAR}");
		}
		if (record.getAuthority() != null) {
			sql.SET("authority = #{authority,jdbcType=VARCHAR}");
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
		if (record.getMenuId() != null) {
			sql.SET("menu_id = #{menuId,jdbcType=BIGINT}");
		}
		sql.WHERE("id = #{id,jdbcType=BIGINT}");
		return sql.toString();
	}

}