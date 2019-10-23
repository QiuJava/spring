package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ShiroRole;

public interface ShiroRoleMapper{
	
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time from shiro_rigth where role_code = #{rigthCode}")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroRoleMapper.BaseResultMap")
	ShiroRole findShiroRoleByRoleCode(@Param("roleCode")String roleCode);
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time from shiro_role where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroRoleMapper.BaseResultMap")
	ShiroRole findShiroRoleById(@Param("id")Integer id);
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time from shiro_role ")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroRoleMapper.BaseResultMap")
	List<ShiroRole> findAllShiroRole();
	
	@Update("update shiro_role set role_code = #{shiroRole.roleCode},role_name= #{shiroRole.roleName},role_remake= #{shiroRole.roleRemake},update_time= #{shiroRole.updateTime} where id = #{shiroRole.id}")
	int updateShiroRole(@Param("shiroRole")ShiroRole shiroRole);
	
	@Insert("insert into shiro_role(role_code,role_name,role_remake,role_state,create_operator,create_time) "
			+ "values(#{shiroRole.roleCode},#{shiroRole.roleName},#{shiroRole.roleRemake},#{shiroRole.roleState},#{shiroRole.createOperator},#{shiroRole.createTime})")
	int insertShiroRole(@Param("shiroRole")ShiroRole shiroRole);
	
	@Delete("delete from shiro_role where id = #{id}")
	int deleteShiroRoleById(@Param("id")Integer id);
	

	@SelectProvider( type=SqlProvider.class,method="findAllShiroRoleList")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroRoleMapper.BaseResultMap")
	List<ShiroRole> findAllShiroRoleList(@Param("shiroRole")ShiroRole shiroRole, @Param("sort")Sort sort, Page<ShiroRole> page);
	

	public class SqlProvider{
		
		
		public String findAllShiroRoleList(final Map<String, Object> parameter) {
			final ShiroRole shiroRole = (ShiroRole) parameter.get("shiroRole");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT(" id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time ");
				FROM(" shiro_role ");
				if (!StringUtils.isBlank(shiroRole.getRoleCode()) )
					WHERE(" role_code like  \"%\"#{shiroRole.roleCode}\"%\" ");
				if (!StringUtils.isBlank(shiroRole.getRoleName()))
					WHERE(" role_name like  \"%\"#{shiroRole.roleName}\"%\" ");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"roleCode","roleName","roleRemake"};
		    final String[] columns={"role_Code","role_name","role_remake"};
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
