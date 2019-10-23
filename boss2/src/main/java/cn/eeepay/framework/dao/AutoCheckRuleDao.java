package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.AutoCheckRoute;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.AutoCheckRule;
import cn.eeepay.framework.model.SysConfigAutoCheck;

public interface AutoCheckRuleDao {
	
	@Select("select * from sys_config where param_key = #{paramKey} ")
	@ResultType(SysConfigAutoCheck.class)
	SysConfigAutoCheck selectByParamKey(@Param("paramKey")String paramKey);

	@Select("select * from live_verify_channel where channel_code = #{channelCode} ")
	@ResultType(AutoCheckRoute.class)
	AutoCheckRoute selectByChannelCode(@Param("channelCode")String channelCode);

	@Update("update sys_config set PARAM_VALUE=#{value} where PARAM_KEY=#{column}")
	int updateValue(@Param("column")String column,@Param("value")String value);

	@Update("update live_verify_channel set percent=#{value} where channel_code=#{column}")
	int updateRoute(@Param("column")String column,@Param("value")String value);
	
	@Select("select * from auto_check_rule")
	@ResultType(AutoCheckRule.class)
	List<AutoCheckRule> selectAll();

	@Update("update auto_check_rule set is_open=#{autoCheckRule.isOpen},is_pass=#{autoCheckRule.isPass} where id=#{autoCheckRule.id}")
	int updateState(@Param("autoCheckRule")AutoCheckRule autoCheckRule);

	@Update("update auto_check_rule set is_open=#{isOpen} where id=#{id}")
	int updateIsOpen(AutoCheckRule info);
	
	@Update("update auto_check_rule set is_pass=#{isPass} where id=#{id}")
	int updateIsPass(AutoCheckRule info);

	@Select({"SELECT ", 
			"	id,percent,route_type  ", 
			"FROM ", 
			"	live_verify_channel  ", 
			"WHERE ", 
			"	route_type = #{routeType}  ", 
			"	AND `STATUS` = 1"})
	List<AutoCheckRoute> findByRouteType(@Param("routeType")int routeType);

	@Update({"UPDATE live_verify_channel  ", 
			"SET percent = #{percent} ", 
			"WHERE ", 
			"	id = #{id}"})
	int updateRoutePercent(Map autoCheckRule);

}
