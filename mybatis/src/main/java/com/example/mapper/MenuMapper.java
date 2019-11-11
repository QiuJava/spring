package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import com.example.entity.Menu;
import com.example.mapper.provider.MenuSqlProvider;

/**
 * 菜单数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface MenuMapper {
	@Delete({ "DELETE  ", //
			"FROM ", //
			"	menu  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int deleteByPrimaryKey(Long id);

	@Insert({ "INSERT INTO menu ( id, menu_name, menu_code, intro, create_time, update_time, parent_id ) ", //
			"VALUES ", //
			"	( #{id,jdbcType=BIGINT} ", //
			"	, #{menuName,jdbcType=VARCHAR} ", //
			"	, #{menuCode,jdbcType=VARCHAR} ", //
			"	, #{intro,jdbcType=VARCHAR} ", //
			"	, #{createTime,jdbcType=TIMESTAMP} ", //
			"	, #{updateTime,jdbcType=TIMESTAMP} ", //
			"	, #{parentId,jdbcType=BIGINT} ", //
			"	)" })
	int insert(Menu record);

	@InsertProvider(type = MenuSqlProvider.class, method = "insertSelective")
	int insertSelective(Menu record);

	@Select({ "SELECT ", //
			"	id, ", //
			"	menu_name, ", //
			"	menu_code, ", //
			"	intro, ", //
			"	create_time, ", //
			"	update_time, ", //
			"	parent_id  ", //
			"FROM ", //
			"	menu  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "menu_name", property = "menuName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "menu_code", property = "menuCode", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.BIGINT) })
	Menu selectByPrimaryKey(Long id);

	@UpdateProvider(type = MenuSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(Menu record);

	@Update({ "UPDATE menu  ", //
			"SET menu_name = #{menuName,jdbcType=VARCHAR}, ", //
			"menu_code = #{menuCode,jdbcType=VARCHAR}, ", //
			"intro = #{intro,jdbcType=VARCHAR}, ", //
			"create_time = #{createTime,jdbcType=TIMESTAMP}, ", //
			"update_time = #{updateTime,jdbcType=TIMESTAMP}, ", //
			"parent_id = #{parentId,jdbcType=BIGINT} ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int updateByPrimaryKey(Menu record);

	@Select({ "SELECT ", //
			"	id, ", //
			"	menu_name, ", //
			"	menu_code, ", //
			"	intro, ", //
			"	parent_id  ", //
			"FROM ", //
			"	menu  ", //
			"WHERE ", //
			"	parent_id IS NULL" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "menu_name", property = "menuName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "menu_code", property = "menuCode", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.BIGINT),
			@Result(column = "id", property = "pemissionList", many = @Many(select = "com.example.mapper.PermissionMapper.selectByMenuId", fetchType = FetchType.EAGER)),
			@Result(column = "id", property = "children", many = @Many(select = "com.example.mapper.MenuMapper.selectByParentId", fetchType = FetchType.EAGER)) })
	List<Menu> selectAll();

	@Select({ "SELECT ", //
			"	id, ", //
			"	menu_name, ", //
			"	menu_code, ", //
			"	intro, ", //
			"	parent_id  ", //
			"FROM ", //
			"	menu  ", //
			"WHERE ", //
			"	parent_id = #{parentId,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "menu_name", property = "menuName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "menu_code", property = "menuCode", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.BIGINT),
			@Result(column = "id", property = "pemissionList", many = @Many(select = "com.example.mapper.PermissionMapper.selectByMenuId", fetchType = FetchType.EAGER)),
			@Result(column = "id", property = "children", many = @Many(select = "com.example.mapper.MenuMapper.selectByParentId", fetchType = FetchType.EAGER)) })
	List<Menu> selectByParentId(Long parentId);

}