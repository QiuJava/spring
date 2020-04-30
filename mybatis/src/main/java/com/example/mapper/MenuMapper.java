package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
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

	int deleteByPrimaryKey(Integer id);

	int insert(Menu record);

	int insertSelective(Menu record);

	Menu selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Menu record);

	int updateByPrimaryKey(Menu record);

	int deleteById(Long id);

	List<MenuTree> listMenuTreeByParentId(Long parentId);

	List<Menu> listByParentId(Long parentId);

	int deleteByParentId(Long parentId);

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

	String getMenuNameById(Integer id);
	
	List<Menu> listByQo(MenuQo qo);
}