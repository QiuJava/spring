package cn.eeepay.framework.dao.bill;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SysMenu;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface SysMenuMapper {
	@Insert("insert into sys_menu(parent_id,menu_name,menu_code,menu_url,rigth_code,menu_level,menu_type,order_no)"
			+"values(#{sysMenu.parentId},#{sysMenu.menuName},#{sysMenu.menuCode},#{sysMenu.menuUrl},#{sysMenu.rigthCode},#{sysMenu.menuLevel},#{sysMenu.menuType},#{sysMenu.orderNo})"
			)
	@SelectKey(statement=" SELECT LAST_INSERT_ID() AS id", keyProperty="sysMenu.id", before=false, resultType=int.class)
	int insertSysMenu(@Param("sysMenu")SysMenu sysMenu);
	
	@Update("update sys_menu set parent_id = #{sysMenu.parentId},menu_name = #{sysMenu.menuName},menu_code= #{sysMenu.menuCode},menu_url= #{sysMenu.menuUrl},"
			+ " rigth_code= #{sysMenu.rigthCode},menu_level= #{sysMenu.menuLevel},menu_type= #{sysMenu.menuType},order_no= #{sysMenu.orderNo} where id = #{sysMenu.id}")
	int updateSysMenu(@Param("sysMenu")SysMenu sysMenu);
	
	
	@Delete("delete from sys_menu where id = #{id} ")
	int deleteSysMenu(@Param("id")Integer id);
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from sys_menu where id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	SysMenu findSysMenuById(@Param("id")Integer id);
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from sys_menu where menu_type='menu' and menu_code = #{menuCode} ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	SysMenu findSysMenuByMenuCodeWithMenu(@Param("menuCode")String menuCode);
	
	@Select("select * from sys_menu where id=(select parent_id from sys_menu where menu_type = 'menu' and menu_code = #{menuCode} ) ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	SysMenu findParentSysMenuByMenuCodeWithMenu(@Param("menuCode")String menuCode);
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from sys_menu where menu_type='page' and menu_code = #{menuCode} ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	SysMenu findSysMenuByMenuCodeWithPage(@Param("menuCode")String menuCode);
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from sys_menu where menu_type='menu' and menu_url = #{menuUrl} ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	SysMenu findSysMenuByMenuUrlWithMenu(@Param("menuUrl")String menuUrl);
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from sys_menu where menu_type='menu' order by order_no ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	List<SysMenu> findAllSysMenu();
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from sys_menu where menu_type='menu' and menu_level=0 order by order_no ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	List<SysMenu> findAllSysMenuAndChildrenList();
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from sys_menu where menu_url is not null order by order_no ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	List<SysMenu> findAllNotBlankUrlSysMenu();
	
	@Select("select id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons from sys_menu where menu_type='page' and parent_id= #{parentId} order by order_no")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
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
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	List<SysMenu> findUserRolePrivilegeSysMenu(@Param("userId")Integer userId);
	
	@SelectProvider(type = SqlProvider.class, method = "findAllPageByParentId")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	List<SysMenu> findAllPageByParentId(@Param("parentId")Integer parentId, @Param("sort")Sort sort);
	
	public class SqlProvider {
		public String findAllPageByParentId(Map<String, Object> parameter) {
			Sort sort = (Sort) parameter.get("sort");
			Integer parentId = (Integer) parameter.get("parentId");
			
			return new SQL() {{
				SELECT("id,parent_id,menu_code,menu_name,menu_url,rigth_code,menu_level,menu_type,order_no,icons");
				FROM("sys_menu");
				WHERE("menu_type = 'page'");
				if (parentId != null) {
					WHERE("parent_id = #{parentId}");
				}
				if(StringUtils.isNotBlank(sort.getSidx())){
					ORDER_BY(propertyMapping(sort.getSidx(), 0)+" "+sort.getSord());
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","menuName","menuCode","menuUrl","orderNo"};
		    final String[] columns={"id","menu_name","menu_code","menu_url","order_no"};
		    if(StringUtils.isNotBlank(name)){
		    	if(type==0){//属性查出字段名
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(propertys[i])){
		    				return columns[i];
		    			}
		    		}
		    	}else if(type==1){//字段名查出属性
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(columns[i])){
		    				return propertys[i];
		    			}
		    		}
		    	}
		    }
			return null;
		}
	}
}
