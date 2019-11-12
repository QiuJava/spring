package com.example.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

import com.example.entity.Permission;
import com.example.qo.PermissionQo;
import com.example.util.StrUtil;

/**
 * 权限Sql供应
 * 
 * @author Qiu Jian
 *
 */
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
		if (record.getUrl() != null) {
			sql.VALUES("url", "#{url,jdbcType=VARCHAR}");
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
		if (record.getUrl() != null) {
			sql.SET("url = #{url,jdbcType=VARCHAR}");
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

	public String selectByQo(PermissionQo qo) {
		SQL sql = new SQL();
		StringBuilder builder = new StringBuilder();
		builder.append("	id, ");
		builder.append("	permission_name, ");
		builder.append("	authority, ");
		builder.append("	url, ");
		builder.append("	intro, ");
		builder.append("	create_time, ");
		builder.append("	update_time, ");
		builder.append("	menu_id  ");

		sql.SELECT(builder.toString());
		sql.FROM("permission");
		if (StrUtil.hasText(qo.getPermissionName())) {
			sql.WHERE("permission_name=#{permissionName,jdbcType=VARCHAR}");
		}
		if (StrUtil.hasText(qo.getAuthority())) {
			sql.WHERE("authority=#{authority,jdbcType=VARCHAR}");
		}
		return sql.toString();
	}

}