package com.example.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

/**
 * 菜单Sql供应
 * 
 * @author Qiu Jian
 *
 */
public class MenuSqlProvider {


	public String selectMenuTreeVoByParentId(Long parentId) {
		SQL sql = new SQL();
		sql.SELECT(new String[] { "id", //
				"menu_name" });
		sql.FROM("menu");
		if (parentId == null) {
			sql.WHERE("parent_id is null");
		} else {
			sql.WHERE("parent_id = #{parentId,jdbcType=BIGINT}");
		}
		return sql.toString();
	}

}