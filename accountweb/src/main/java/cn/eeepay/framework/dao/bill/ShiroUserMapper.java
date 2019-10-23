package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

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
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ShiroUser;
import cn.eeepay.framework.model.bill.User;

public interface ShiroUserMapper{
	
	@Select("SELECT * FROM shiro_user ")
	@ResultType(ShiroUser.class)
	List<ShiroUser> findAllShiroUser();
	

	@SelectProvider( type=SqlProvider.class,method="findShiroUserNameByParams")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroUserMapper.BaseResultMap")
	List<ShiroUser> findShiroUserNameByParams(@Param("user")ShiroUser user);
	
	
	/**
	 * 新增用户信息
	 */
	@Insert("insert into shiro_user(user_Name,`password`,real_Name,tel_No,email,state,theme,create_Operator,create_Time,dept_id)"
			+ " values(#{user.userName},#{user.password},#{user.realName},#{user.telNo},#{user.email},#{user.state},"
			+ "#{user.theme},#{user.createOperator},#{user.createTime},#{user.deptId})")
	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="user.id", before=false, resultType=int.class)  
	int insert(@Param("user")ShiroUser user);
	
	@Insert("insert into user(name, age,password,creattime)" 
			+ " values(#{user.name},#{user.age},#{user.password},#{user.creatTime})")
	int insertTestUser(@Param("user")User user);
	
	@Update("update shiro_user set user_name = #{shiroUser.userName},real_name= #{shiroUser.realName},tel_no= #{shiroUser.telNo},email= #{shiroUser.email},state= #{shiroUser.state},dept_id= #{shiroUser.deptId} where id = #{shiroUser.id}")
	int updateUser(@Param("shiroUser")ShiroUser shiroUser);
	
	@Delete("delete from shiro_user where id = #{id} ")
	int deleteUserById(@Param("id")Integer id);
	
	@Update("update shiro_user set password = #{password} where id = #{id}")
	int updateUserPwd(@Param("id")Integer id,@Param("password")String password);
	
	@SelectProvider(type=SqlProvider.class,method="findUsers")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroUserMapper.BaseResultMap")
	List<ShiroUser> findUsers(@Param("user")ShiroUser user,@Param("sort")Sort sort,Page<ShiroUser> page);
	
	@Select("select * from shiro_user")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroUserMapper.OneToManyResultMap")
	List<ShiroUser> findUsersWithRole2();
	
//	@Select("select * from shiro_user")
//	@ResultMap("cn.eeepay.framework.dao.bill.ShiroUserMapper.BaseResultMap")
//	List<ShiroUser> findAllUsers();
	
	@Select("select * from shiro_user where user_name =#{userName}")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroUserMapper.OneToManyResultMap")
	ShiroUser findUserWithRolesByUserName(@Param("userName")String userName);
	
	@Select("select * from shiro_user where user_name =#{userName}")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroUserMapper.BaseResultMap")
	ShiroUser findUserByUserName(@Param("userName")String userName);
	
	@Select("select * from shiro_user where id =#{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.ShiroUserMapper.BaseResultMap")
	ShiroUser findUserById(@Param("id")Integer id);
	
	public class SqlProvider{
		public String findShiroUserNameByParams(final Map<String, Object> parameter) {
			final ShiroUser user = (ShiroUser) parameter.get("user");
			return new SQL(){{
				SELECT(" user_name,real_name ");
				FROM("shiro_user");
				if (!StringUtils.isBlank(user.getUserName()))
					WHERE(" user_name like  \"%\"#{user.userName}\"%\" or real_name like  \"%\"#{user.userName}\"%\" ");
			}}.toString();
		}
		public String findUsers(Map<String,Object> param){
			final ShiroUser user=(ShiroUser)param.get("user");
			final Sort sord=(Sort)param.get("sort");
			return new SQL(){{
				SELECT("id,user_Name,`password`,real_name,tel_no,email,state,theme,create_operator,create_time,state,dept_id");
				FROM("shiro_user");
				if(StringUtils.isNotBlank(user.getUserName())){
					WHERE("user_name like \"%\" #{user.userName} \"%\" ");
				}
				if(StringUtils.isNotBlank(user.getRealName())){
					WHERE("real_name like \"%\" #{user.realName} \"%\" ");
				}
				if(StringUtils.isNotBlank(user.getEmail())){
					WHERE("email like \"%\" #{user.email} \"%\" ");
				}
				if(StringUtils.isNotBlank(user.getTelNo())){
					WHERE("tel_no like \"%\" #{user.telNo} \"%\" ");
				}
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","userName","password","realName","telNo","email","state","theme",
		    		"createOperator","createTime","deptId"};
		    final String[] columns={"id","user_name","password","real_name","tel_no","email","state","theme",
		    		"create_operator","create_time","dept_id"};
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
