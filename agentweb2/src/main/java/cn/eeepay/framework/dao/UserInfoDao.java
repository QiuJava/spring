package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.StringUtil;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.UserEntityInfo;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.util.Constants;

@WriteReadDataSource
public interface UserInfoDao{
	/**
	 * 新增用户信息
	 */
	@Insert("insert into user_info(user_id,user_name,mobilephone,email,status,update_pwd_time,password,team_id,create_time,second_user_node)"
			+ " values(#{userInfo.userId},#{userInfo.userName},#{userInfo.mobilephone},#{userInfo.email},#{userInfo.status},#{userInfo.updatePwdTime},#{userInfo.password},"
			+ "#{userInfo.teamId},#{userInfo.createTime},#{userInfo.secondUserNode})")
	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="userInfo.id", before=false, resultType=int.class)  
	int insert(@Param("userInfo")UserInfo userInfo);
	
	/*@Update("update user_info set user_name = #{userInfo.userName},mobilephone= #{userInfo.mobilephone},status= #{userInfo.status}"
			+ " ,update_pwd_time= #{userInfo.updatePwdTime},password= #{userInfo.password} ,team_id= #{userInfo.teamId} ,create_time= #{userInfo.createTime}"
			+ " where user_id = #{userInfo.userId}")*/
	@UpdateProvider(type=SqlProvider.class,method="updateUserInfoByUserId")
	int updateUserInfoByUserId(@Param("userInfo")UserInfo userInfo);
	
	// 只删除在user_entity_info中没有数据的user_info
	@Delete("delete u from user_info u left join user_entity_info e on u.user_id = e.user_id where u.id = #{id} and e.id is null")
	int deleteUserInfoById(@Param("id")Integer id);
	
	// 只删除在user_entity_info中没有数据的user_info
	@Delete("delete u from user_info u left join user_entity_info e on u.user_id = e.user_id where u.user_id = #{userId} and e.id is null")
	int deleteUserInfoByUserId(@Param("userId")String userId);
	
	@Update("update user_info set password = #{password},update_pwd_time= now() where user_id = #{userId}")
	int updateUserPwdByUserId(@Param("userId")String userId,@Param("password")String password);

	@Select("select * from user_info")
	@ResultMap("cn.eeepay.framework.dao.UserInfoMapper.BaseResultMap")
	List<UserInfo> findAllUserInfo();
	
	@Select("select * from user_info where mobilephone =#{mobilePhone}")
	@ResultMap("cn.eeepay.framework.dao.UserInfoMapper.OneToManyResultMap")
	UserInfo findUsersWithRoleByMobilePhone(@Param("mobilePhone")String mobilePhone);
	
	@Select("select * from user_info ui,user_entity_info uei where ui.user_id=uei.user_id and uei.apply=1 and uei.user_type=1 and ui.team_id = #{teamId} and ui.mobilephone =#{mobilePhone} ")
	@ResultType(UserInfo.class)
	UserInfo findUserInfoByMobilePhoneAndTeamId(@Param("mobilePhone")String mobilePhone,@Param("teamId")String teamId);
	
	@Select("select * from user_info ui,user_entity_info uei where ui.user_id=uei.user_id  and uei.apply=1 and uei.user_type=1 and ui.team_id = #{teamId} and ui.email =#{email}")
	@ResultType(UserInfo.class)
	UserInfo findUserInfoByEmailAndTeamId(@Param("email")String email, @Param("teamId")String teamId);
	
	@Select("select * from user_info where user_id =#{userId}")
	@ResultMap("cn.eeepay.framework.dao.UserInfoMapper.BaseResultMap")
	UserInfo findUserInfoByUserId(@Param("userId")String userId);
	
	@Select("select * from user_info where mobilephone=#{telNo} and team_id=#{teamId}")
	@ResultType(UserInfo.class)
	UserInfo selectInfoByTelNo(@Param("telNo")String telNo,@Param("teamId")String teamId);
	
	@Update("update user_info set password=#{pwd},mobilephone=#{newTelNo},update_pwd_time=now() where "
			+ "mobilephone=#{telNo} and team_id=#{teamId}")
	int updateInfoByMp(@Param("telNo")String telNo,@Param("newTelNo")String newTelNo,@Param("teamId")String teamId,@Param("pwd")String pwd);

	@Update("UPDATE user_info SET wrong_password_count = wrong_password_count + 1 WHERE user_id = #{userId}")
	void increaseWrongPasswordCount(@Param("userId") String userId);

	@Update("UPDATE user_info SET wrong_password_count = 0, lock_time=now() WHERE user_id = #{userId}")
	void lockLoginUser(@Param("userId") String userInfoId);

	@Update("UPDATE user_info SET wrong_password_count = 0 WHERE user_id = #{userId}")
	void clearWrongPasswordCount(@Param("userId") String userInfoId);

	@Update("update user_entity_info set access_team_id=#{accessTeamId} where user_id=#{userId}")
	int changeAccessTeamId(@Param("userId") String userId, @Param("accessTeamId") String accessTeamId);

	public class SqlProvider{
		
		public String updateUserInfoByUserId(Map<String,Object> param){
			final UserInfo user=(UserInfo)param.get("userInfo");
			return new SQL(){{
				UPDATE("user_info");
				SET("user_name = #{userInfo.userName},mobilephone= #{userInfo.mobilephone},email=#{userInfo.email},status= #{userInfo.status},team_id= #{userInfo.teamId}");
				if(StringUtils.isNotBlank(user.getPassword())){
					SET("update_pwd_time= now(),password= #{userInfo.password}");
				}
				WHERE("user_id = #{userInfo.userId}");
			}}.toString();
		}
	}

	@Select("select nextval('user_no_seq')")
	@ResultType(value = Integer.class)
	public int selectUserNoSeq();


	@Select("select * from user_info ui left join user_entity_info uei on ui.user_id = uei.user_id where ui.mobilephone = #{phone} and uei.entity_id = #{agentNo}")
	@ResultType(UserInfo.class)
	UserInfo selectUserInfo(@Param("phone")String phone, @Param("agentNo")Long orgId);
	/**
	 * 更新二级代理节点
	 * @param userId
	 * @param secondUserNode
	 * @return
	 */
	@Update("update user_info set second_user_node = #{secondUserNode} where user_id = #{userId} ")
	int updateSecondUserNode(@Param("userId")String userId,@Param("secondUserNode")String secondUserNode);

	/**
	 * 查询超级银行家角色ID
	 * @return
	 */
	@Select("select id from agent_shiro_role where role_code = 'SUPER_BANK'")
	@ResultType(Integer.class)
	Integer selectSuperBankRole();

	/**
	 * 根据二级代理节点查询用户
	 * @param secondUserNode
	 * @return
	 */
	@Select("select * from user_info where second_user_node = #{secondUserNode} ")
	@ResultType(UserInfo.class)
	UserInfo selectBySecondUserNode(@Param("secondUserNode")String secondUserNode);

	/**
	 * 根据二级代理节点修改手机号
	 * @param secondUserNode
	 * @param phone
	 * @param password
	 * @return
	 */
	@Update("update user_info set password = #{password},update_pwd_time = now(),mobilephone = #{phone} where second_user_node = #{secondUserNode}")
	int updatePhoneBySecondUserNode(@Param("secondUserNode")String secondUserNode, @Param("phone")String phone, @Param("password")String password);
	
}
