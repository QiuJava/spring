package cn.eeepay.framework.dao.bill;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.SystemInfo;
/**
 * 系统日切日终正常等状态表
 * @author Administrator
 *
 */
public interface SystemInfoMapper {
	
	@Select( "select * from system_info where current_date = #{currentDate}")
	@ResultMap("cn.eeepay.framework.dao.bill.SystemInfoMapper.BaseResultMap")
	SystemInfo findSystemInfoByCurrentDate(@Param("currentDate")String currentDate);
	
	@Select( "select * from system_info where id = #{Id}")
	@ResultMap("cn.eeepay.framework.dao.bill.SystemInfoMapper.BaseResultMap")
	SystemInfo findSystemInfoById(@Param("Id")Integer Id);
	
	@Update("update system_info set status = #{systemInfo.status},`current_date`= #{systemInfo.currentDate},parent_trans_date= #{systemInfo.parentTransDate},next_trans_date= #{systemInfo.nextTransDate} where id = #{systemInfo.id}")
	int updateSystemInfo(@Param("systemInfo")SystemInfo systemInfo);
}
