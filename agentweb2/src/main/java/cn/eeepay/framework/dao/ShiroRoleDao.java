package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ShiroRole;
import cn.eeepay.framework.service.ShiroRoleService.QueryParam;
import cn.eeepay.framework.service.ShiroRoleService.ShiroRole2;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

@WriteReadDataSource
public interface ShiroRoleDao{

	public class SqlProvider {
		@SuppressWarnings("rawtypes")
		public String findRoles(Map query) {
			final QueryParam param = (QueryParam) query.get("param");
			return new SQL() {
				{
					SELECT("sr.*, (select count(*) from agent_user_role where role_id=sr.id) as user_count");
					FROM("agent_shiro_role sr");
					if (StringUtils.isBlank(param.getAgentNo()) || "ALL".equals(param.getAgentNo()))
						WHERE("sr.scope is null");
					else
						WHERE("sr.scope = #{param.agentNo}");
					WHERE("sr.role_name like #{param.roleNameStr}");
					WHERE("sr.role_code like #{param.roleCodeStr}");
				}
			}.toString();
		}
	}

	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time,scope from agent_shiro_rigth where role_code = #{rigthCode}")
	@ResultMap("cn.eeepay.framework.dao.ShiroRoleMapper.BaseResultMap")
	ShiroRole findShiroRoleByRoleCode(@Param("roleCode")String roleCode);
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time,scope from agent_shiro_role where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.ShiroRoleMapper.BaseResultMap")
	ShiroRole findShiroRoleById(@Param("id")Integer id);
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time,scope from agent_shiro_role ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRoleMapper.BaseResultMap")
	List<ShiroRole> findAllShiroRole();
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time,scope from agent_shiro_role where scope is null ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRoleMapper.BaseResultMap")
	List<ShiroRole> findAdminShiroRole();
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time,scope from agent_shiro_role where scope = #{agentId} ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRoleMapper.BaseResultMap")
	List<ShiroRole> findAgentShiroRole(@Param("agentId")String agentId);
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time,scope from agent_shiro_role where scope = #{agentId} "
			+ "or id in (5,6,(select id from agent_shiro_role where role_code = 'SUPER_BANK')) ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRoleMapper.BaseResultMap")
	List<ShiroRole> findUsableAgentShiroRole(@Param("agentId")String agentId);
	
	@Update("update agent_shiro_role set role_code = #{shiroRole.roleCode},role_name= #{shiroRole.roleName},role_remake= #{shiroRole.roleRemake},update_time= #{shiroRole.updateTime},scope= #{shiroRole.scope} where id = #{shiroRole.id}")
	int updateShiroRole(@Param("shiroRole")ShiroRole shiroRole);
	
	@Insert("insert into agent_shiro_role(role_code,role_name,role_remake,role_state,create_operator,create_time,scope) "
			+ "values(#{shiroRole.roleCode},#{shiroRole.roleName},#{shiroRole.roleRemake},#{shiroRole.roleState},#{shiroRole.createOperator},#{shiroRole.createTime},#{shiroRole.scope})")
	int insertShiroRole(@Param("shiroRole")ShiroRole shiroRole);

	@Delete("delete from agent_shiro_role where id=#{id}")
	int deleteShiroRoleById(@Param("id") Integer roleId);

	@SelectProvider(type = SqlProvider.class, method = "findRoles")
	@ResultType(ShiroRole2.class)
	List<ShiroRole2> findRoles(@Param("param") QueryParam param, @Param("page") Page<ShiroRole2> page);

	@Select("SELECT\n" + 
			"	a_s_r.role_name\n" + 
			"FROM\n" + 
			"	agent_shiro_role a_s_r\n" + 
			"JOIN agent_user_role a_u_r ON a_s_r.id = a_u_r.role_id\n" + 
			"AND a_u_r.user_id = #{userId} LIMIT 1 ")
	@ResultType(String.class)
	String findRoleNameByUserId(Integer userId);
	
	@Select("SELECT\n" + 
			"	a_s_r.role_name\n" + 
			"FROM\n" + 
			"	agent_shiro_role_oem a_s_r_o\n" + 
			"JOIN agent_shiro_role a_s_r ON a_s_r_o.role_id = a_s_r.id\n" + 
			"WHERE\n" + 
			"	a_s_r_o.agent_type = #{agentType}\n" + 
			"AND a_s_r_o.agent_oem = #{agentOem} LIMIT 1")
	@ResultType(String.class)
	String findRoleNameByOem(@Param("agentType")String agentType, @Param("agentOem")String agentOem);
	
	
	@Select("SELECT\n" + 
			"	a_s_r.role_name\n" + 
			"FROM\n" + 
			"	agent_shiro_role_oem a_s_r_o\n" + 
			"JOIN agent_shiro_role a_s_r ON a_s_r_o.role_id = a_s_r.id\n" + 
			"WHERE\n" + 
			"	a_s_r_o.agent_type = #{agentType}\n" + 
			"AND a_s_r_o.agent_oem = '' LIMIT 1")
	@ResultType(String.class)
	String findRoleNameByAgentType(@Param("agentType")String agentType, @Param("agentOem")String agentOem);
	
	@Select("SELECT\n" + 
			"	a_s_r.role_name\n" + 
			"FROM\n" + 
			"	agent_shiro_role_oem a_s_r_o\n" + 
			"JOIN agent_shiro_role a_s_r ON a_s_r_o.role_id = a_s_r.id\n" + 
			"WHERE\n" + 
			"	a_s_r_o.id = 1 LIMIT 1\n")
	@ResultType(String.class)
	String findRoleNameByDefault();

	@Select("SELECT\r\n" + 
			"	count(*)\r\n" + 
			"FROM\r\n" + 
			"	agent_shiro_role_oem t\r\n" + 
			"WHERE\r\n" + 
			"	t.role_id = #{id}")
	@ResultType(Long.class)
	long findCountByRoleId(Integer id);
}
