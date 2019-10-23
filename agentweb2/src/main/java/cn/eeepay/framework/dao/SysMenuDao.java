package cn.eeepay.framework.dao;
import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.SysMenu;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
@WriteReadDataSource
public interface SysMenuDao {
	@Insert("insert into agent_sys_menu(parent_id,menu_name,menu_code,menu_url,rigth_code,menu_level,menu_type,order_no)"
			+"values(#{sysMenu.parentId},#{sysMenu.menuName},#{sysMenu.menuCode},#{sysMenu.menuUrl},#{sysMenu.rigthCode},#{sysMenu.menuLevel},#{sysMenu.menuType},#{sysMenu.orderNo})"
			)
	@SelectKey(statement=" SELECT LAST_INSERT_ID() AS id", keyProperty="sysMenu.id", before=false, resultType=int.class)
	int insertSysMenu(@Param("sysMenu")SysMenu sysMenu);
	
	@Update("update agent_sys_menu set parent_id = #{sysMenu.parentId},menu_name = #{sysMenu.menuName},menu_code= #{sysMenu.menuCode},menu_url= #{sysMenu.menuUrl},"
			+ " rigth_code= #{sysMenu.rigthCode},menu_level= #{sysMenu.menuLevel},menu_type= #{sysMenu.menuType},order_no= #{sysMenu.orderNo} where id = #{sysMenu.id}")
	int updateSysMenu(@Param("sysMenu")SysMenu sysMenu);
	
	
	@Delete("delete from agent_sys_menu where id = #{id} ")
	int deleteSysMenu(@Param("id")Integer id);
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from agent_sys_menu where id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.SysMenuMapper.BaseResultMap")
	SysMenu findSysMenuById(@Param("id")Integer id);
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from agent_sys_menu where menu_code = #{menuCode} ")
	@ResultMap("cn.eeepay.framework.dao.SysMenuMapper.BaseResultMap")
	SysMenu findSysMenuByMenuCode(@Param("menuCode")String menuCode);
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from agent_sys_menu where menu_type='menu' order by order_no ")
	@ResultMap("cn.eeepay.framework.dao.SysMenuMapper.BaseResultMap")
	List<SysMenu> findAllSysMenu();
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from agent_sys_menu where menu_type='menu' and menu_level=0 order by order_no ")
	@ResultMap("cn.eeepay.framework.dao.SysMenuMapper.OneToManyResultMap")
	List<SysMenu> findAllSysMenuAndChildrenList();
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from agent_sys_menu where menu_url is not null order by order_no ")
	@ResultMap("cn.eeepay.framework.dao.SysMenuMapper.BaseResultMap")
	List<SysMenu> findAllNotBlankUrlSysMenu();
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from agent_sys_menu where menu_type='page' and parent_id= #{parentId} ")
	@ResultMap("cn.eeepay.framework.dao.SysMenuMapper.BaseResultMap")
	List<SysMenu> findAllPageSysMenuByParentId(@Param("parentId")Integer parentId);
	
	@Select("select DISTINCT sys_menu.id,sys_menu.parent_id,sys_menu.menu_code,sys_menu.menu_name,sys_menu.menu_url,sys_menu.rigth_code,sys_menu.menu_level,sys_menu.menu_type,sys_menu.order_no,sys_menu.icons"
			+ " from shiro_user,user_role,shiro_role,role_rigth,shiro_rigth,sys_menu"
			+ " where shiro_user.id = user_role.user_id"
			+ " and user_role.role_id = shiro_role.id"
			+ " and shiro_role.id = role_rigth.role_id"
			+ " and role_rigth.rigth_id = shiro_rigth.id"
			+ " and shiro_rigth.rigth_code = sys_menu.menu_code"
			+ " and shiro_user.id=#{userId} "
			+ " and sys_menu.menu_type='menu' "
			+ " order by sys_menu.order_no ")
	@ResultMap("cn.eeepay.framework.dao.SysMenuMapper.BaseResultMap")
	List<SysMenu> findUserRolePrivilegeSysMenu(@Param("userId")Integer userId);
}
