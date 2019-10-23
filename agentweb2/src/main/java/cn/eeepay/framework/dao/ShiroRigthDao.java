package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.ShiroRigth;

@WriteReadDataSource
public interface ShiroRigthDao{
	
	@Insert("insert into agent_shiro_rigth(rigth_code,rigth_name,rigth_comment,rigth_type)"
			+"values(#{shiroRigth.rigthCode},#{shiroRigth.rigthName},#{shiroRigth.rigthComment},#{shiroRigth.rigthType})"
			)
	int insertShiroRigth(@Param("shiroRigth")ShiroRigth shiroRigth);
	
	@Delete("delete from agent_shiro_rigth where rigth_code = #{rigthCode} ")
	int deleteShiroRigthByRigthCode(@Param("rigthCode")String rigthCode);
	
	@Select("select * from agent_shiro_rigth where rigth_code = #{rigthCode}")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	ShiroRigth findShiroRigthByRigthCode(@Param("rigthCode")String rigthCode);
	
	@Select("select id,rigth_code,rigth_name,rigth_comment,rigth_type from agent_shiro_rigth where id =#{id}")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.OneToManyResultMap")
	ShiroRigth findShiroRigthById(@Param("id")Integer id);
	
	@Select("select * from agent_shiro_rigth ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findAllShiroRigth();
	
	@Select("select DISTINCT agent_shiro_rigth.id,agent_shiro_rigth.rigth_code,agent_shiro_rigth.rigth_name,agent_shiro_rigth.rigth_comment,agent_shiro_rigth.rigth_type, agent_sys_menu.order_no "
			+ " from user_entity_info ue,agent_user_role,agent_shiro_role,agent_role_rigth,agent_shiro_rigth,agent_sys_menu"
			+ " where ue.id = agent_user_role.user_id"
			+ " and agent_user_role.role_id = agent_shiro_role.id"
			+ " and agent_shiro_role.id = agent_role_rigth.role_id"
			+ " and agent_role_rigth.rigth_id = agent_shiro_rigth.id"
			+ " and agent_shiro_rigth.rigth_code = agent_sys_menu.menu_code"
			+ " and ue.id=#{userId} "
			+ " and ue.user_type = 1 "
			+ " order by agent_sys_menu.order_no ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findUserRolePrivilegeRigth(@Param("userId")Integer userId);
	
	
	@Select("select agent_shiro_rigth.id,agent_shiro_rigth.rigth_code,agent_shiro_rigth.rigth_name,agent_shiro_rigth.rigth_comment,agent_shiro_rigth.rigth_type, agent_sys_menu.order_no "
			+ " from user_entity_info ue,agent_user_role,agent_shiro_role,agent_role_rigth,agent_shiro_rigth,agent_sys_menu"
			+ " where ue.id = agent_user_role.user_id"
			+ " and agent_user_role.role_id = agent_shiro_role.id"
			+ " and agent_shiro_role.id = agent_role_rigth.role_id"
			+ " and agent_role_rigth.rigth_id = agent_shiro_rigth.id"
			+ " and agent_shiro_rigth.rigth_code = agent_sys_menu.menu_code"
			+ " and ue.id = #{userId} "
			+ " and ue.user_type = 1 "
			+ " and agent_sys_menu.parent_id = #{parentId} "
			+ " order by agent_sys_menu.order_no ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findUserWithRolesPrivilegeRigthByParentId(@Param("userId")Integer userId,@Param("parentId")Integer parentId);
	
	
	@Select(" select agent_shiro_rigth.id,agent_shiro_rigth.rigth_code,agent_shiro_rigth.rigth_name,agent_shiro_rigth.rigth_comment,agent_shiro_rigth.rigth_type, agent_sys_menu.order_no "+
			"  from agent_role_rigth,agent_shiro_rigth,agent_sys_menu "+
			"  where  agent_role_rigth.rigth_id = agent_shiro_rigth.id "+
			"  and agent_shiro_rigth.rigth_code = agent_sys_menu.menu_code "+
			"  and agent_sys_menu.parent_id = #{parentId}"+ 
			"  and agent_role_rigth.role_id = #{roleId}  "+
			"  order by agent_sys_menu.order_no ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findRolePrivilegeRigthByParentId(@Param("roleId")Integer roleId,@Param("parentId")Integer parentId);
	
	@Select(" select r.id,r.rigth_code,r.rigth_name,r.rigth_comment,r.rigth_type  "+
			" from agent_sys_menu as m,agent_shiro_rigth as r "+
			" where m.menu_code = r.rigth_code "+
			" and m.parent_id = #{parentId} "+ 
			" and m.menu_type='page' ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findShireRigthByParentId(@Param("parentId")Integer parentId);

	@Select("SELECT " + 
			"	count(a_s_role.id) " + 
			"FROM " + 
			"	agent_user_role a_u_r " + 
			"JOIN agent_shiro_role a_s_role ON a_u_r.role_id = a_s_role.id " + 
			"AND a_u_r.user_id = #{uId} " + 
			"AND a_s_role.create_operator = 'admin' ")
	long countAdminCreateRoleByUid(Integer uId);

	@Select("SELECT DISTINCT " + 
			"	a_s_r.id, " + 
			"	a_s_r.rigth_code, " + 
			"	a_s_r.rigth_name, " + 
			"	a_s_r.rigth_comment, " + 
			"	a_s_r.rigth_type, " + 
			"	a_s_m.order_no " + 
			"FROM " + 
			"	agent_info a_i " + 
			"JOIN agent_shiro_role_oem a_s_r_o ON a_s_r_o.agent_type = a_i.agent_type " + 
			"JOIN agent_role_rigth a_r_r ON a_s_r_o.role_id = a_r_r.role_id " + 
			"JOIN agent_shiro_rigth a_s_r ON a_s_r.id = a_r_r.rigth_id " + 
			"JOIN agent_sys_menu a_s_m ON a_s_r.rigth_code = a_s_m.menu_code " + 
			"AND  " + 
			"	a_i.agent_no = #{agentNo} " + 
			" " + 
			"AND a_s_r_o.agent_oem = a_i.agent_oem " + 
			"ORDER BY " + 
			"	a_s_m.order_no")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findRigthByOem(@Param("agentNo")String agentNo);

	@Select("SELECT DISTINCT " + 
			"	a_s_r.id, " + 
			"	a_s_r.rigth_code, " + 
			"	a_s_r.rigth_name, " + 
			"	a_s_r.rigth_comment, " + 
			"	a_s_r.rigth_type, " + 
			"	a_s_m.order_no " + 
			"FROM " + 
			"   agent_shiro_role_oem a_s_r_o " + 
			"JOIN agent_role_rigth a_r_r ON a_s_r_o.role_id = a_r_r.role_id " + 
			"JOIN agent_shiro_rigth a_s_r ON a_s_r.id = a_r_r.rigth_id " + 
			"JOIN agent_sys_menu a_s_m ON a_s_r.rigth_code = a_s_m.menu_code " + 
			"AND a_s_r_o.id = 1 " + 
			"ORDER BY " + 
			"	a_s_m.order_no")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findRigthByDefault();

	@Select("SELECT DISTINCT " + 
			"	a_s_r.id, " + 
			"	a_s_r.rigth_code, " + 
			"	a_s_r.rigth_name, " + 
			"	a_s_r.rigth_comment, " + 
			"	a_s_r.rigth_type, " + 
			"	a_s_m.order_no " + 
			"FROM " + 
			"	agent_info a_i " + 
			"JOIN agent_shiro_role_oem a_s_r_o ON a_s_r_o.agent_type = a_i.agent_type " + 
			"JOIN agent_role_rigth a_r_r ON a_s_r_o.role_id = a_r_r.role_id " + 
			"JOIN agent_shiro_rigth a_s_r ON a_s_r.id = a_r_r.rigth_id " + 
			"JOIN agent_sys_menu a_s_m ON a_s_r.rigth_code = a_s_m.menu_code " + 
			"AND  " + 
			"	a_i.agent_no = #{agentNo} " + 
			" " + 
			"AND a_s_r_o.agent_oem='' " +
			"ORDER BY " + 
			"	a_s_m.order_no")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findRigthByAgentType(@Param("agentNo")String agentNo);
	
//	public class SqlProvider{
//		public String findUsers(Map<String,Object> param){
//			final ShiroUser user=(ShiroUser)param.get("user");
//			final Sort sord=(Sort)param.get("sort");
//			return new SQL(){{
//				SELECT("user_Name,`password`,real_Name,tel_No,email,state,theme,create_Operator,create_Time");
//				FROM("shiro_user");
//				if(StringUtils.isNotBlank(user.getUserName())){
//					WHERE("user_name=#{user.userName}");
//				}
//				if(StringUtils.isNotBlank(user.getRealName())){
//					WHERE("real_name=#{user.realName}");
//				}
//				if(StringUtils.isNotBlank(user.getEmail())){
//					WHERE("email=#{user.email}");
//				}
//				if(StringUtils.isNotBlank(sord.getSidx())){
//					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
//				}
//			}}.toString();
//		}
//		public String propertyMapping(String name,int type){
//			final String[] propertys={"userName","password","realName","telNo","email","state","theme",
//		    		"createOperator","createTime"};
//		    final String[] columns={"user_Name","password","real_Name","tel_No","email","state","theme",
//		    		"create_Operator","create_Time"};
//		    if(StringUtils.isNotBlank(name)){
//		    	if(type==0){//属性查出字段名
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(propertys[i])){
//		    				return columns[i];
//		    			}
//		    		}
//		    	}else if(type==1){//字段名查出属性
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(columns[i])){
//		    				return propertys[i];
//		    			}
//		    		}
//		    	}
//		    }
//			return null;
//		}
//	}
}
