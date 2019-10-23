package cn.eeepay.framework.dao.bill;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.UserSetting;

public interface UserSettingMapper{
	/**
	 * 新增用户设置
	 */
	@Insert("insert into user_setting(user_id,back_last_page,collapse_menu)"
			+ " values(#{userSetting.userId},#{userSetting.backLastPage},#{userSetting.collapseMenu})")
	int insert(@Param("userSetting")UserSetting userSetting);
	
	@Update("update user_setting set back_last_page = #{userSetting.backLastPage},collapse_menu = #{userSetting.collapseMenu}"
			+ " where user_id = #{userSetting.userId}")
	int update(@Param("userSetting")UserSetting userSetting);
	
	@Delete("delete from user_setting where user_id = #{userId} ")
	int deleteUserSettingByUserId(@Param("userId")Integer userId);
	
	@Select("select * from user_setting where user_id = #{userId} ")
	@ResultMap("cn.eeepay.framework.dao.bill.UserSettingMapper.BaseResultMap")
	UserSetting findUserSettingByUserId(@Param("userId")Integer userId);
	
	@Select("select * from user_setting ")
	@ResultMap("cn.eeepay.framework.dao.bill.UserSettingMapper.BaseResultMap")
	List<UserSetting> findAllUserSetting();
	
}
