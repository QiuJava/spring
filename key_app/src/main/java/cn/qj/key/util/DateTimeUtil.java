package cn.qj.key.util;

import java.util.Date;

/**
 * 日期时间工具
 * 
 * @author Qiujian
 * @date 2018/12/17
 */
public class DateTimeUtil {
	private DateTimeUtil() {
	}
	
	public static final String DATATIME_PATTERN  = "yyyy-MM-dd HH:mm:ss";
	public static final String DATA_PATTERN  = "yyyy-MM-dd";
	public static final String MONTH_PATTERN  = "yyyyMM";
	
	public static Date getDate(){
		return new Date();
	}
	
	public static Date getDate(Long milliscond) {
		return new Date(milliscond);
	}
}
