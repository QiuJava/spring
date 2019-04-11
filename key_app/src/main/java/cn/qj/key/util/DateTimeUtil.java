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
	
	/**
	 * 发送手机验证吗间隔秒数
	 */
	public static final int SEND_AUTHCODE_SPACE_SECOND = 60;
	/**
	 * 手机验证码有效时间 3分钟
	 */
	public static final int AUTHCODE_VALID = 60 * 3;
	
	
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
