package com.example.mapper.provider;

import com.example.entity.Menu;
import org.apache.ibatis.jdbc.SQL;

/**
 * 菜单Sql供应
 * 
 * @author Qiu Jian
 *
 */
public class MenuSqlProvider {

	public String insertSelective(Menu record) {
		SQL sql = new SQL();
		sql.INSERT_INTO("menu");

		if (record.getId() != null) {
			sql.VALUES("id", "#{id,jdbcType=BIGINT}");
		}

		if (record.getMenuName() != null) {
			sql.VALUES("menu_name", "#{menuName,jdbcType=VARCHAR}");
		}

		if (record.getMenuCode() != null) {
			sql.VALUES("menu_code", "#{menuCode,jdbcType=VARCHAR}");
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

		if (record.getParentId() != null) {
			sql.VALUES("parent_id", "#{parentId,jdbcType=BIGINT}");
		}

		return sql.toString();
	}

	public String updateByPrimaryKeySelective(Menu record) {
		SQL sql = new SQL();
		sql.UPDATE("menu");

		if (record.getMenuName() != null) {
			sql.SET("menu_name = #{menuName,jdbcType=VARCHAR}");
		}

		if (record.getMenuCode() != null) {
			sql.SET("menu_code = #{menuCode,jdbcType=VARCHAR}");
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

		if (record.getParentId() != null) {
			sql.SET("parent_id = #{parentId,jdbcType=BIGINT}");
		}

		sql.WHERE("id = #{id,jdbcType=BIGINT}");

		return sql.toString();
	}
}