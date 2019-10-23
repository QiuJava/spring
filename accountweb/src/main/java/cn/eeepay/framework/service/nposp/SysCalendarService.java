package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.model.nposp.SysCalendar;

public interface SysCalendarService {
	SysCalendar findSysCalendarBySysDate(String sysDate) throws Exception;
	boolean isHoliday(String sysDate) throws Exception;
}
