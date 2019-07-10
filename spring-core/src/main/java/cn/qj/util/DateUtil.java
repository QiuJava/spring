package cn.qj.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public class DateUtil {
	private DateUtil() {
	}

	public static final String DATATIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATA_PATTERN = "yyyy-MM-dd";
	public static final String YEAR_MONTH_PATTERN = "yyyyMM";

	/**
	 * 获取当前时间
	 * 
	 * @return new Date()
	 */
	public static Date getNewDate() {
		return new Date();
	}

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
		SimpleDateFormat format = new SimpleDateFormat(YEAR_MONTH_PATTERN, Locale.CHINA);
		return format.format(date);
	}

}
