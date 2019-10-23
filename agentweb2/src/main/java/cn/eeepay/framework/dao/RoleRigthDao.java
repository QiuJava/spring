package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.RoleRigth;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
@WriteReadDataSource
public interface RoleRigthDao{
	
	@Insert("insert into agent_role_rigth(role_id,rigth_id) values(#{roleId},#{rigthId})")
	int insertRoleRigth(@Param("roleId")Integer roleId,@Param("rigthId")Integer rigthId);
	
	@Delete("delete from agent_role_rigth where role_id = #{roleId} and rigth_id = #{rigthId}")
	int deleteRoleRigth(@Param("roleId")Integer roleId,@Param("rigthId")Integer rigthId);
	
	@Select(" select rr.id,rr.role_id,rr.rigth_id,sr.id as rigth_id,sr.rigth_code,sr.rigth_name,sr.rigth_comment,sr.rigth_type "+
			" from agent_role_rigth as rr,agent_shiro_rigth as sr "+
			" where rr.rigth_id = sr.id "+
			" and rr.role_id = #{roleId} ")
	@ResultMap("cn.eeepay.framework.dao.RoleRigthMapper.OneToOneResultMap")
	List<RoleRigth> findRoleRigthByRoleId(@Param("roleId")Integer roleId);

	@Delete("delete from agent_role_rigth where role_id = #{roleId}")
	int deleteRoleRigthByRoleId(@Param("roleId")Integer roleId);

}
