package cn.qj;

import org.junit.Test;

import cn.qj.util.DateUtil;

public class DateUtilTest {

	@Test
	public void testName() throws Exception {
		// 获取当前时间的日期时间
		System.out.println(DateUtil.getDateTimeStr(DateUtil.getNewDate()));
		System.out.println(DateUtil.getDateStr(DateUtil.getNewDate()));
		System.out.println(DateUtil.getYearMonthStr(DateUtil.getNewDate()));
		
	}

}
