package cn.eeepay.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.FunctionManager;
import cn.eeepay.framework.model.IndustrySwitch;

public interface FunctionManagerDao {

	@Select(" select * from function_manage ")
	@ResultType(FunctionManager.class)
	List<FunctionManager> selectFunctionManagers();

	@Update("update function_manage set function_switch=#{functionSwitch} where id=#{id}")
	int updateFunctionSwitch(FunctionManager functionManager);

	@Update("update function_manage set agent_control=#{agentControl} where id=#{id}")
	int updateAgentControl(FunctionManager functionManager);

	@Select(" select * from function_manage where id=#{id}")
	@ResultType(FunctionManager.class)
	FunctionManager getFunctionManager(int id);

	@Select(" select * from function_manage where function_number=#{funcNum}")
	@ResultType(FunctionManager.class)
	FunctionManager getFunctionManagerByNum(@Param("funcNum") String funcNum);

	@Select("select * from industry_switch order by start_time")
	@ResultType(IndustrySwitch.class)
	List<IndustrySwitch> findAll();
	
	@Insert("INSERT INTO `industry_switch`( `acq_merchant_type`, `start_time`, `end_time`, `create_time`) VALUES ( #{acqMerchantType},STR_TO_DATE(#{startTime}, '%H:%i') ,STR_TO_DATE(#{endTime}, '%H:%i'),#{createTime})")
	int industrySwitchSave(IndustrySwitch data);
	
	@Insert("INSERT INTO `industry_switch`( `acq_merchant_type`, `start_time`, `end_time`, `create_time`) VALUES ( #{acqMerchantType},STR_TO_DATE(#{startTime}, '%H:%i') ,STR_TO_DATE(#{endTime}, '%H:%i:%s'),#{createTime})")
	int industrySwitchSaveData(IndustrySwitch data);

	@Delete("delete from industry_switch where id=#{id}")
	int industrySwitchDelete(Long id);

	@Select("SELECT " + 
			"	count(*)  " + 
			"FROM " + 
			"	industry_switch  " + 
			"WHERE " + 
			"	( STR_TO_DATE( #{startTime}, '%H:%i' ) < end_time AND STR_TO_DATE(  #{startTime}, '%H:%i' ) >= start_time )  " + 
			"	OR ( STR_TO_DATE( #{endTime}, '%H:%i' ) > start_time AND STR_TO_DATE( #{endTime}, '%H:%i' ) <= end_time ) OR (STR_TO_DATE( #{startTime}, '%H:%i' )<=start_time and STR_TO_DATE( #{endTime}, '%H:%i' ) >end_time )")
	long industrySwitchCount(@Param("startTime")String startTime, @Param("endTime")String endTime);

	@Insert("INSERT INTO `industry_switch`(`id`, `acq_merchant_type`, `start_time`, `end_time`, `create_time`) VALUES ( #{id},#{acqMerchantType},STR_TO_DATE(#{startTime}, '%H:%i') ,STR_TO_DATE(#{endTime}, '%H:%i'),#{createTime})")
	int industrySwitchSaveAll(IndustrySwitch data);

	@Update("update sys_dict set sys_value=#{sysValue} where sys_key=#{sysKey}")
	int updateSysDictValue(@Param("sysKey")String string, @Param("sysValue")Integer industrySwitch);

	@Insert("INSERT INTO `industry_switch`(`id`, `acq_merchant_type`, `start_time`, `end_time`, `create_time`) VALUES ( #{id},#{acqMerchantType},STR_TO_DATE(#{startTime}, '%H:%i') ,STR_TO_DATE(#{endTime}, '%H:%i:%s'),#{createTime})")
	int industrySwitchSaveAllData(IndustrySwitch data);
}
