package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.UserRole;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
@WriteReadDataSource
public interface UserRoleDao{
	
	
	@Insert("insert into agent_user_role(user_id,role_id) values(#{userId},#{roleId})")
	int insertUserRole(@Param("userId")Integer userId,@Param("roleId")Integer roleId);
	
	
	@Delete("delete from agent_user_role where user_id = #{userId} and role_id = #{role_id}")
	int deleteUserRole(@Param("userId")Integer userId,@Param("role_id")Integer role_id);
	
	@Delete("delete from agent_user_role where user_id = #{userId}")
	int deleteUserRoleByUserId(@Param("userId")Integer userId);
	
	@Select(" select ur.id,ur.user_id,ur.role_id,sr.id as role_id,sr.role_code,sr.role_name,sr.role_remake, "+
			" sr.role_state,sr.create_operator,sr.create_time,sr.update_time  "+
			" from agent_user_role as ur,agent_shiro_role sr "+
			" where ur.role_id = sr.id "+
			" and ur.user_id=#{userId} ")
	@ResultMap("cn.eeepay.framework.dao.UserRoleMapper.OneToOneResultMap")
	List<UserRole> findUserRoleByUserId(@Param("userId")Integer userId);

	@Select(" select ur.id,ur.user_id,ur.role_id,sr.id as role_id,sr.role_code,sr.role_name,sr.role_remake, "
			+ " sr.role_state,sr.create_operator,sr.create_time,sr.update_time  "
			+ " from agent_user_role as ur,agent_shiro_role sr "
			+ " where ur.role_id = sr.id "
			+ " and ur.role_id=#{roleId} ")
	@ResultMap("cn.eeepay.framework.dao.UserRoleMapper.BaseResultMap")
	List<UserRole> findUserRoleByRoleId(@Param("roleId") Integer roleId);

	@Select("SELECT\r\n" + 
			"	sr.id AS role_id,\r\n" + 
			"	sr.role_code,\r\n" + 
			"	sr.role_name,\r\n" + 
			"	sr.role_remake,\r\n" + 
			"	sr.role_state,\r\n" + 
			"	sr.create_operator,\r\n" + 
			"	sr.create_time,\r\n" + 
			"	sr.update_time\r\n" + 
			"FROM\r\n" + 
			"	agent_shiro_role sr\r\n" + 
			"WHERE\r\n" + 
			" sr.scope = #{agentNo}")
	@ResultMap("cn.eeepay.framework.dao.UserRoleMapper.OneToOneResultMap")
	List<UserRole> findUserRoleByUserIdNew(@Param("agentNo")String agentNo);

	@Select("SELECT\r\n" + 
			"	sr.id AS role_id,\r\n" + 
			"	sr.role_code,\r\n" + 
			"	sr.role_name,\r\n" + 
			"	sr.role_remake,\r\n" + 
			"	sr.role_state,\r\n" + 
			"	sr.create_operator,\r\n" + 
			"	sr.create_time,\r\n" + 
			"	sr.update_time\r\n" + 
			"FROM\r\n" + 
			"	agent_shiro_role sr\r\n" + 
			"JOIN agent_shiro_role_oem a_s_r_o ON sr.id = a_s_r_o.role_id\r\n" + 
			"JOIN agent_info a_i ON a_s_r_o.agent_type = a_i.agent_type\r\n" + 
			"AND a_i.agent_no = #{entityId}\r\n" + 
			"AND a_s_r_o.agent_oem = a_i.agent_oem")
	@ResultMap("cn.eeepay.framework.dao.UserRoleMapper.OneToOneResultMap")
	List<UserRole> findUserRoleByOem(String entityId);

	@Select("SELECT\r\n" + 
			"	sr.id ,\r\n" + 
			"	sr.id AS role_id,\r\n" + 
			"	sr.role_code,\r\n" + 
			"	sr.role_name,\r\n" + 
			"	sr.role_remake,\r\n" + 
			"	sr.role_state,\r\n" + 
			"	sr.create_operator,\r\n" + 
			"	sr.create_time,\r\n" + 
			"	sr.update_time\r\n" + 
			"FROM\r\n" + 
			"	agent_shiro_role sr\r\n" + 
			"JOIN agent_shiro_role_oem a_s_r_o ON sr.id = a_s_r_o.role_id\r\n" + 
			"JOIN agent_info a_i ON a_s_r_o.agent_type = a_i.agent_type\r\n" + 
			"AND a_i.agent_no = #{entityId}\r\n" + 
			"AND a_s_r_o.agent_oem = ''"
			)
	@ResultMap("cn.eeepay.framework.dao.UserRoleMapper.OneToOneResultMap")
	List<UserRole> findUserRoleByAgentType(String entityId);

	@Select("SELECT\r\n" + 
			"	sr.id AS role_id,\r\n" + 
			"	sr.role_code,\r\n" + 
			"	sr.role_name,\r\n" + 
			"	sr.role_remake,\r\n" + 
			"	sr.role_state,\r\n" + 
			"	sr.create_operator,\r\n" + 
			"	sr.create_time,\r\n" + 
			"	sr.update_time\r\n" + 
			"FROM\r\n" + 
			"	agent_shiro_role sr\r\n" + 
			"JOIN agent_shiro_role_oem a_s_r_o ON sr.id = a_s_r_o.role_id\r\n" + 
			"AND a_s_r_o.id = 1")
	@ResultMap("cn.eeepay.framework.dao.UserRoleMapper.OneToOneResultMap")
	List<UserRole> findUserRoleByDefault(String entityId);


}
