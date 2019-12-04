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

	public String selectByQo(PermissionQo qo) {
		SQL sql = new SQL();

		sql.SELECT(new String[] { "permi_0.id AS id", //
				"permi_0.permission_name AS permission_name", //
				"permi_0.authority AS authority", //
				"permi_0.url AS url", //
				"permi_0.intro AS intro", //
				"menu_0.id AS menu_id" //
		});
		sql.FROM("permission permi_0");
		sql.JOIN("menu menu_0 ON menu_0.id = permi_0.menu_id");
		if (StrUtil.hasText(qo.getPermissionName())) {
			sql.WHERE("permi_0.permission_name=#{permissionName,jdbcType=VARCHAR}");
		}
		if (StrUtil.hasText(qo.getAuthority())) {
			sql.WHERE("permi_0.authority=#{authority,jdbcType=VARCHAR}");
		}
		if (qo.getMenuId() != null) {
			sql.WHERE("menu_0.id=#{menuId,jdbcType=VARCHAR}");
		}
		return sql.toString();
	}

	public String insert(Permission permission) {
		SQL sql = new SQL();
		sql.INSERT_INTO("permission");
		sql.VALUES("permission_name", "#{permissionName,jdbcType=VARCHAR}");
		sql.VALUES("authority", "#{authority,jdbcType=VARCHAR}");
		sql.VALUES("menu_id", "#{menuId,jdbcType=BIGINT}");
		sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
		sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
		if (StrUtil.hasText(permission.getUrl())) {
			sql.VALUES("url", "#{url,jdbcType=VARCHAR}");
		}
		if (StrUtil.hasText(permission.getIntro())) {
			sql.VALUES("intro", "#{intro,jdbcType=VARCHAR}");
		}
		return sql.toString();
	}

	public String updateById(Permission permission) {
		SQL sql = new SQL();
		sql.UPDATE("permission");
		if (StrUtil.hasText(permission.getPermissionName())) {
			sql.SET("permission_name=#{permissionName,jdbcType=VARCHAR}");
		}
		if (StrUtil.hasText(permission.getAuthority())) {
			sql.SET("authority=#{authority,jdbcType=VARCHAR}");
		}
		sql.SET("url=#{url,jdbcType=VARCHAR}");
		sql.SET("intro=#{intro,jdbcType=VARCHAR}");
		sql.SET("update_time=#{updateTime,jdbcType=TIMESTAMP}");
		sql.WHERE("id = #{id,jdbcType=BIGINT}");
		return sql.toString();
	}

}