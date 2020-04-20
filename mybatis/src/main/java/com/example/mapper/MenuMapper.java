package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import com.example.entity.Menu;
import com.example.entity.MenuTree;
import com.example.mapper.provider.MenuSqlProvider;
import com.example.qo.MenuQo;
import com.example.vo.MenuTreeVo;

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
	int deleteById(Long id);

	@InsertProvider(type = MenuSqlProvider.class, method = "insert")
	int insert(Menu record);

	@UpdateProvider(type = MenuSqlProvider.class, method = "updateById")
	int updateById(Menu record);

	@SelectProvider(type = MenuSqlProvider.class, method = "selectMenuTreeByParentId")
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "menu_name", property = "text", jdbcType = JdbcType.VARCHAR),
			@Result(column = "url", property = "url", jdbcType = JdbcType.VARCHAR),
			@Result(column = "id", property = "children", many = @Many(select = "com.example.mapper.MenuMapper.selectMenuTreeByParentId", fetchType = FetchType.EAGER)) })
	List<MenuTree> selectMenuTreeByParentId(Long parentId);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	menu  ", //
			"WHERE ", //
			"	menu_name = #{menuName,jdbcType=VARCHAR}" })
	int countByMenuName(String menuName);

	@Select({ "SELECT ", //
			"	menu_name ", //
			"FROM ", //
			"	menu  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	String selectMenuNameById(Long id);

	@SelectProvider(type = MenuSqlProvider.class, method = "selectByParentId")
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.BIGINT),
			@Result(column = "menu_name", property = "menuName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "url", property = "url", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "id", property = "children", many = @Many(select = "com.example.mapper.MenuMapper.selectByParentId", fetchType = FetchType.EAGER)) })
	List<Menu> selectByParentId(Long parentId);

	@Delete({ "DELETE  ", //
			"FROM ", //
			"	menu  ", //
			"WHERE ", //
			"	parent_id = #{parentId,jdbcType=BIGINT}" })
	int deleteByParentId(Long parentId);

	@SelectProvider(type = MenuSqlProvider.class, method = "selectByQo")
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.BIGINT),
			@Result(column = "menu_name", property = "menuName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "url", property = "url", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "id", property = "children", many = @Many(select = "com.example.mapper.MenuMapper.selectByParentId", fetchType = FetchType.EAGER)) })
	List<Menu> selectByQo(MenuQo qo);

	@SelectProvider(type = MenuSqlProvider.class, method = "selectMenuTreeVoByParentId")
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "menu_name", property = "text", jdbcType = JdbcType.VARCHAR),
			@Result(column = "id", property = "children", many = @Many(select = "com.example.mapper.MenuMapper.selectMenuTreeVoByParentId", fetchType = FetchType.EAGER)) })
	List<MenuTreeVo> selectMenuTreeVoByParentId(Long parentId);

	@Select({"SELECT ", // 
			"	DISTINCT permis_1.menu_id  ", // 
			"FROM ", // 
			"	role_permission role_permis_0 ", // 
			"	JOIN permission permis_1 ON permis_1.id = role_permis_0.permission_id ", // 
			"	AND role_permis_0.role_id = #{roleId,jdbcType=BIGINT}"})
	List<Long> selectMenuTreeByRoleId(Long roleId);

}