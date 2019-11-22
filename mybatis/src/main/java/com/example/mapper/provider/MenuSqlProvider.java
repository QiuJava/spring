package com.example.mapper.provider;

import com.example.entity.Menu;
import com.example.qo.MenuQo;
import com.example.util.StrUtil;

import org.apache.ibatis.jdbc.SQL;

/**
 * 菜单Sql供应
 * 
 * @author Qiu Jian
 *
 */
public class MenuSqlProvider {

	public String selectByQo(MenuQo qo) {
		SQL sql = new SQL();
		sql.SELECT(new String[] { "menu_0.id AS id ", //
				"	menu_0.menu_name AS menu_name ", //
				"	menu_0.intro AS intro ", //
				"	menu_0.create_time AS create_time ", //
				"	menu_0.update_time AS update_time ", //
				"	parent_menu_0.menu_name AS parent_menu_name " });
		sql.FROM("menu menu_0");
		sql.LEFT_OUTER_JOIN("menu parent_menu_0 ON parent_menu_0.id = menu_0.parent_id");
		if (StrUtil.hasText(qo.getMenuName())) {
			sql.WHERE("menu_0.menu_name = #{menuName,jdbcType=VARCHAR}");
		}
		if (StrUtil.hasText(qo.getParentMenuName())) {
			sql.WHERE("parent_menu_0.menu_name = #{parentMenuName,jdbcType=VARCHAR}");
		}
		return sql.toString();
	}

	public String selectMenuTreeVoByParentId(Long parentId) {
		SQL sql = new SQL();
		sql.SELECT(new String[] { "id", //
				"menu_name", //
				"url" });
		sql.FROM("menu");
		if (parentId == null) {
			sql.WHERE("parent_id is null");
		} else {
			sql.WHERE("parent_id = #{parentId,jdbcType=BIGINT}");
		}
		return sql.toString();
	}

	public String insert(Menu record) {
		SQL sql = new SQL();
		sql.INSERT_INTO("menu");
		sql.VALUES("menu_name", "#{menuName,jdbcType=VARCHAR}");
		if (StrUtil.hasText(record.getIntro())) {
			sql.VALUES("intro", "#{intro,jdbcType=VARCHAR}");
		}
		if (record.getParentId() != null) {
			sql.VALUES("parent_id", "#{parentId,jdbcType=BIGINT}");
		}
		sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
		sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");

		return sql.toString();
	}

	public String updateById(Menu record) {
		SQL sql = new SQL();
		sql.UPDATE("menu");

		if (record.getMenuName() != null) {
			sql.SET("menu_name = #{menuName,jdbcType=VARCHAR}");
		}
		if (StrUtil.hasText(record.getIntro())) {
			sql.SET("intro = #{intro,jdbcType=VARCHAR}");
		}
		sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
		sql.WHERE("id = #{id,jdbcType=BIGINT}");
		return sql.toString();
	}
}