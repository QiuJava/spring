package com.example.mapper;

import com.example.entity.Menu;
import com.example.mapper.provider.MenuSqlProvider;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

/**
 * 菜单数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface MenuMapper {
	@Delete({ "delete from menu", "where id = #{id,jdbcType=BIGINT}" })
	int deleteByPrimaryKey(Long id);

	@Insert({ "insert into menu (id, menu_name, ", "menu_code, intro, ", "create_time, update_time, ", "parent_id)",
			"values (#{id,jdbcType=BIGINT}, #{menuName,jdbcType=VARCHAR}, ",
			"#{menuCode,jdbcType=VARCHAR}, #{intro,jdbcType=VARCHAR}, ",
			"#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP}, ", "#{parentId,jdbcType=BIGINT})" })
	int insert(Menu record);

	@InsertProvider(type = MenuSqlProvider.class, method = "insertSelective")
	int insertSelective(Menu record);

	@Select({ "select", "id, menu_name, menu_code, intro, create_time, update_time, parent_id", "from menu",
			"where id = #{id,jdbcType=BIGINT}" })
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

	@Update({ "update menu", "set menu_name = #{menuName,jdbcType=VARCHAR},",
			"menu_code = #{menuCode,jdbcType=VARCHAR},", "intro = #{intro,jdbcType=VARCHAR},",
			"create_time = #{createTime,jdbcType=TIMESTAMP},", "update_time = #{updateTime,jdbcType=TIMESTAMP},",
			"parent_id = #{parentId,jdbcType=BIGINT}", "where id = #{id,jdbcType=BIGINT}" })
	int updateByPrimaryKey(Menu record);
}