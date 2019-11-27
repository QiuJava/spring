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
		sql.SELECT(new String[] { "id", //
				"menu_name", //
				"intro", //
				"create_time", //
				"update_time", //
				"parent_id", //
				"url" });
		sql.FROM("menu");
		
		if (StrUtil.hasText(qo.getMenuName())) {
			sql.WHERE("menu_name = #{menuName,jdbcType=VARCHAR}");
		}else {
			if (qo.getParentId() == null) {
				sql.WHERE("parent_id is null");
			} else {
				sql.WHERE("parent_id = #{parentId,jdbcType=BIGINT}");
			}
		}
		return sql.toString();
	}

	public String selectMenuTreeByParentId(Long parentId) {
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

	public String selectByParentId(Long parentId) {
		SQL sql = new SQL();
		sql.SELECT(new String[] { "id", //
				"menu_name", //
				"intro", //
				"create_time", //
				"update_time", //
				"parent_id", //
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
		sql.VALUES("url", "#{url,jdbcType=VARCHAR}");
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
		sql.SET("intro = #{intro,jdbcType=VARCHAR}");
		if (StrUtil.hasText(record.getUrl())) {
			sql.SET("url = #{url,jdbcType=VARCHAR}");
		}
		sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
		sql.WHERE("id = #{id,jdbcType=BIGINT}");
		return sql.toString();
	}
}