package cn.loan.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;

/**
 * 日期工具
 * 
 * @author Qiujian
 * 
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

	/**
	 * 是否过期
	 * 
	 * @param date
	 * @param second
	 * @return
	 */
	public static boolean isOverdue(Date date, Integer second) {
		return getNewDate().getTime() / 1000 > date.getTime() / 1000 + second;
	}

	/**
	 * 相差的秒
	 * 
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	public static long dimSs(Date sDate, Date eDate) {
		return Math.abs(sDate.getTime() / 1000 - eDate.getTime() / 1000);
	}

	/**
	 * 获取当天的最后时间
	 * 
	 * @param date 2017-12-12 12:00:00
	 * @return 2017-12-12 23:59:59
	 */
	public static Date getEndTime(Date date) {
		Date truncate = DateUtils.truncate(date, Calendar.DATE);
		Date addDays = DateUtils.addDays(truncate, 1);
		return DateUtils.addSeconds(addDays, -1);
	}

}
