package framework.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.model.nposp.SysCalendar;
import cn.eeepay.framework.service.nposp.SysCalendarService;

public class SysCalendarServiceTest  extends BaseTest {
	
	@Resource
	public SysCalendarService sysCalendarService;
	
	@Test
	public void test1() throws Exception {
//		SysCalendar sysCalendar= sysCalendarService.findSysCalendarBySysDate("2016-10-2");
//		if (sysCalendar !=null) {
//			System.out.println(sysCalendar.getEventName());
//		}
//		else{
//			System.out.println("工作日");
//		}
		
		boolean isHoliday = sysCalendarService.isHoliday("2016-10-2");
		System.out.println(isHoliday);
		
	}
}
