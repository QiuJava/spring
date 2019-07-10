package cn.qj.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;

/**
 * 日期工具
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public class DateUtil {
	private DateUtil() {
	}

	public static final String DATATIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATA_PATTERN = "yyyy-MM-dd";
	public static final String YEAR_MONTH_PATTERN = "yyyyMM";

	/**
	 * 当前时间的明天
	 * 
	 * @param date 2019-06-08 20:59:59
	 * @return 2019-06-09 23:59:59
	 */
	public static Date endOfDay(Date date) {
		Date truncate = DateUtils.truncate(date, Calendar.DATE);
		return DateUtils.addSeconds(DateUtils.addDays(truncate, 1), -1);
	}

	/**
	 * 计算两个时间相差的秒数
	 * 
	 * @param date1
	 * @param date2
	 * @return long 秒
	 */
	public static long calcBetweenSecond(Date date1, Date date2) {
		return Math.abs((date1.getTime() - date2.getTime()) / 1000);
	}

	/**
	 * 判断两个日期时间差小于多少
	 * 
	 * @param date1
	 * @param date2
	 * @param mesc  毫秒
	 * @return boolean
	 */
	public static boolean isBetweenMesc(Date date1, Date date2, long mesc) {
		return date1.getTime() - date2.getTime() <= mesc;
	}

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
