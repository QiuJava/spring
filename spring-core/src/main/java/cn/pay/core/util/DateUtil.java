package cn.pay.core.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

/**
 * 日期工具
 * 
 * @author Qiujian
 *
 */
public class DateUtil {
	private DateUtil() {
	}

	public static final long LOCK_TIME = 1000 * 60;
	public static final long ONE_HOUR = 1000 * 60 * 60;

	/**
	 * 当前时间的明天
	 * 
	 * @param d
	 * @return
	 */
	public static Date endOfDay(Date d) {
		return DateUtils.addSeconds(DateUtils.addDays(DateUtils.truncate(d, Calendar.DATE), 1), -1);
	}

	/**
	 * 计算两个时间相差的秒数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long setBetweenDate(Date d1, Date d2) {
		return Math.abs((d1.getTime() - d2.getTime()) / 1000);
	}

	/**
	 * 判断两个日期时间差小于多少
	 * 
	 * @param d1
	 * @param d2
	 * @param mesc
	 *            毫秒
	 * @return
	 */
	public static boolean isBetweenDate(Date d1, Date d2, long mesc) {
		return Math.abs((d1.getTime() - d2.getTime()) / 1000) <= mesc;
	}

}
