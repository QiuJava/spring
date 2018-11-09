package cn.qj.core.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import cn.qj.core.consts.SysConst;

/**
 * 字符串工具
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public class StringUtil {
	private StringUtil() {
	}

	public static boolean hasLength(String str) {
		return str != null && str.length() > 0;
	}

	public static String map2XmlStr(Map<String, String> map) {
		StringBuffer sb = new StringBuffer("");
		sb.append("<xml>");
		Set<String> set = map.keySet();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			Object value = map.get(key);
			sb.append("<").append(key).append(">");
			sb.append(value);
			sb.append("</").append(key).append(">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public static String signStr(SortedMap<String, String> param, String signKey) {
		StringBuilder sb = new StringBuilder();
		for (String key : param.keySet()) {
			sb.append(key);
			sb.append("=");
			sb.append(param.get(key));
			sb.append("&");
		}
		sb.append("key=");
		sb.append(signKey);
		return SysConst.MD5.encodePassword(sb.toString(), null);
	}

}
