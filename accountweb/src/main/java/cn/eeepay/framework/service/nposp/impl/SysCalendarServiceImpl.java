package cn.eeepay.framework.service.nposp.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.SysCalendarMapper;
import cn.eeepay.framework.model.nposp.SysCalendar;
import cn.eeepay.framework.service.nposp.SysCalendarService;

/**
 * 
 * 节假日
 * @author zouruijin
 * @date 2016年8月16日17:19:42
 */

@Service("sysCalendarService")
@Transactional
public class SysCalendarServiceImpl  implements SysCalendarService{

	@Resource
	public SysCalendarMapper sysCalendarMapper;

	@Override
	public SysCalendar findSysCalendarBySysDate(String sysDate) throws Exception {
		return sysCalendarMapper.findSysCalendarBySysDate(sysDate);
	}

	@Override
	public boolean isHoliday(String sysDate) throws Exception {
		SysCalendar sysCalendar= findSysCalendarBySysDate(sysDate);
		if (sysCalendar!=null) {
			return true;
		}
		return false;
	}


}
