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

import cn.eeepay.framework.model.UserEntityInfo;

@WriteReadDataSource
public interface UserEntityInfoDao{
	@Insert("insert into user_entity_info(user_id,user_type,manage,status,entity_id,apply,last_notice_time)"
			+"values(#{userEntityInfo.userId},#{userEntityInfo.userType},#{userEntityInfo.manage},#{userEntityInfo.status},#{userEntityInfo.entityId},#{userEntityInfo.apply},now())"
			)
	@SelectKey(statement=" SELECT LAST_INSERT_ID() AS id", keyProperty="userEntityInfo.id", before=false, resultType=int.class)  
	int insertUserEntity(@Param("userEntityInfo")UserEntityInfo userEntityInfo);
	
	@Update("update user_entity_info set  manage= #{userEntityInfo.manage}, status= #{userEntityInfo.status} where user_id = #{userEntityInfo.userId} AND apply =#{userEntityInfo.apply} ")
	int updateUserEntity(@Param("userEntityInfo")UserEntityInfo userEntityInfo);
	
	
	@Select("select * from user_entity_info where user_id =#{userId} and user_type = '1' ")
	@ResultMap("cn.eeepay.framework.dao.UserEntityInfoMapper.OneToManyResultMap")
	UserEntityInfo findAgentUserEntityInfoWithRoleByUserId(@Param("userId")String userId);
	
	@Select("select * from user_entity_info where apply=1 and user_type = '1' and user_id =#{userId} ")
	@ResultMap("cn.eeepay.framework.dao.UserEntityInfoMapper.BaseResultMap")
	UserEntityInfo findAgentUserEntityInfoByUserId(@Param("userId")String userId);
	
	@Select("select * from user_entity_info where user_id =#{userId} and user_type = '1' ")
	@ResultMap("cn.eeepay.framework.dao.UserEntityInfoMapper.BaseResultMap")
	List<UserEntityInfo> findAgentUserEntitysInfoByUserId(@Param("userId")String userId);

	@Delete("delete from user_entity_info where id = #{id}")
	int deleteAgentUserEntityById(@Param("id") Integer id);
}
