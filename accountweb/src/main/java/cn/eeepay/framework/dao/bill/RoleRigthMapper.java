package cn.eeepay.framework.dao.bill;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.bill.RoleRigth;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface RoleRigthMapper{
	
//	@Insert("insert into user_rigth(user_id,rigth_id)" 
//			+"values(#{userRigth.userId},#{userRigth.rigthId})"
//			)
//	int insertUserRigth(@Param("userRigth")UserRigth userRigth);
	
	@Insert("insert into role_rigth(role_id,rigth_id) values(#{roleId},#{rigthId})")
	int insertRoleRigth(@Param("roleId")Integer roleId,@Param("rigthId")Integer rigthId);
	
//	@Delete("delete from user_rigth where user_id = #{userRigth.userId} and rigth_id = #{userRigth.rigthId}")
//	int deleteUserRigth(@Param("userRigth")UserRigth userRigth);
	
	@Delete("delete from role_rigth where role_id = #{roleId} and rigth_id = #{rigthId}")
	int deleteRoleRigth(@Param("roleId")Integer roleId,@Param("rigthId")Integer rigthId);
	
	@Select(" select rr.id,rr.role_id,rr.rigth_id,sr.id as rigth_id,sr.rigth_code,sr.rigth_name,sr.rigth_comment,sr.rigth_type "+
			" from role_rigth as rr,shiro_rigth as sr "+
			" where rr.rigth_id = sr.id "+
			" and rr.role_id = #{roleId} ")
	@ResultMap("cn.eeepay.framework.dao.bill.RoleRigthMapper.BaseResultMap")
	List<RoleRigth> findRoleRigthByRoleId(@Param("roleId")Integer roleId);

}
