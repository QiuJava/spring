package cn.eeepay.framework.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Request工具类
 * @author yangle
 *
 */
public class ReqUtil {
	/**
	 * 获取字符串参数
	 * @param request
	 * @param param
	 * @param value
	 * @return
	 */
	public static String getString(ServletRequest request, String param,
			String value) {
		String temp = request.getParameter(param);
		if ((temp != null) && (!(temp.equals("")))) {
			return temp;
		}
		return value;
	}
	
	public static int getInt(ServletRequest request, String param,
			int defaultNum) {
		String temp = request.getParameter(param);
		if ((temp != null) && (!(temp.equals("")))) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception localException) {
			}
			return num;
		}
		return defaultNum;
	}
	
	public static Float getFloat(ServletRequest request, String param,
			Float value) {
		String temp = request.getParameter(param);
		if ((temp != null) && (!(temp.equals("")))) {
			Float num = value;
			try {
				num = Float.valueOf(Float.parseFloat(temp));
			} catch (Exception localException) {
			}
			return num;
		}
		return value;
	}
	
	public static double getDouble(ServletRequest request, String param,
			double value) {
		String temp = request.getParameter(param);
		if ((temp != null) && (!(temp.equals("")))) {
			double num = value;
			try {
				num = Double.parseDouble(temp);
			} catch (Exception localException) {
			}
			return num;
		}
		return value;
	}
	
	public static boolean getBoolean(HttpServletRequest request, String param,
			boolean b) {
		String temp = request.getParameter(param);

		return ((StringUtils.isNotEmpty(temp)) && (((temp
				.equalsIgnoreCase("true")) || (temp.equals("1")))));
	}
}
