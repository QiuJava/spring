package cn.eeepay.framework.dao.nposp;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.SysCalendar;

/**
 * 节假日
 * @author zouruijin
 * @date 2016年8月16日17:19:42
 *
 */
public interface SysCalendarMapper {
	
	@Select(" select * from sys_calendar where sys_date = #{sysDate}")
	@ResultMap("cn.eeepay.framework.dao.nposp.SysCalendarMapper.BaseResultMap")
	public SysCalendar findSysCalendarBySysDate(String sysDate);
}
