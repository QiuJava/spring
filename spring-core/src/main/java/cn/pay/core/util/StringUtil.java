package cn.pay.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统字符串操作工具类
 * 
 * @author Administrator
 *
 */
public class StringUtil {
	private StringUtil() {
	}

	public static boolean hasLength(String str) {
		return str != null && str.length() > 0;
	}

	public static Map<String, String> mapStringToMap(String str) {
		str = str.substring(1, str.length() - 1);
		str = str.replace(" ", "");
		String[] strs = str.split(",");
		Map<String, String> map = new HashMap<String, String>();
		for (String string : strs) {
			String key = string.split("=")[0];
			String value = string.split("=")[1];
			map.put(key, value);
		}
		return map;
	}

}
