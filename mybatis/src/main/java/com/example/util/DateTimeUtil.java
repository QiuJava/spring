package com.example.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间工具
 * 
 * @author Qiu Jian
 */
public class DateTimeUtil {
	private DateTimeUtil() {
	}

	/**
	 * 锁定间隔1分钟
	 */
	public static final long LOCK_INTERVAL = 60 * 1000;
	public static final String DATATIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATA_PATTERN = "yyyy-MM-dd";
	public static final String MONTH_PATTERN = "yyyyMM";

	/**
	 * 日期对象转换为 日期时间字符串
	 * 
	 * @return 1970-1-1 00:00:00
	 */
	public static String getDateTimeStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(DATATIME_PATTERN, Locale.CHINA);
		return format.format(date);
	}

	/**
	 * 日期对象转换为日期字符串
	 * 
	 * @return 1970-1-1
	 */
	public static String getDateStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(DATA_PATTERN, Locale.CHINA);
		return format.format(date);
	}

	/**
	 * 日期对象转换为日期字符串
	 * 
	 * @return 197001
	 */
	public static String getYearMonthStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(MONTH_PATTERN, Locale.CHINA);
		return format.format(date);
	}

	/**
	 * 日期字符串转换为日期对象
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date toDateByPattern(String dateStr, String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
		return format.parse(dateStr);
	}

}
