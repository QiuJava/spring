package cn.eeepay.framework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlUtil {
	/**
  	 * 将 String 转为 map
  	 * 
  	 * @param param
  	 *            aa=11&bb=22&cc=33
  	 * @return
  	 */
  	public static Map<String, Object> getUrlParams(String param) {
  		Map<String, Object> map = new HashMap<String, Object>();
  		if ("".equals(param) || null == param) {
  			return map;
  		}
  		String[] params = param.split("&");
  		for (int i = 0; i < params.length; i++) {
  			String[] p = params[i].split("=");
  			if (p.length == 2) {
  				map.put(p[0], p[1]);
  			}
  		}
  		return map;
  	}
  
  	/**
  	 * 将map 转为 string
  	 * 
  	 * @param map
  	 * @return
  	 */
  	public static String getUrlParamsByMap(Map<String, Object> map,
  			boolean isSort) {
  		if (map == null) {
  			return "";
  		}
  		StringBuffer sb = new StringBuffer();
  		List<String> keys = new ArrayList<String>(map.keySet());
  		if (isSort) {
  			Collections.sort(keys);
  		}
  		for (int i = 0; i < keys.size(); i++) {
  			String key = keys.get(i);
  			String value = map.get(key).toString();
  			sb.append(key +"=" + value);
  			sb.append("&");
  		}
  		String s = sb.toString();
  		if (s.endsWith("&")) {
  			s = s.substring(0, s.lastIndexOf("&"));
  		}
  		/*
  		 * for (Map.Entry<String, Object> entry : map.entrySet()) {
  		 * sb.append(entry.getKey() + "=" + entry.getValue()); sb.append("&"); }
  		 * String s = sb.toString(); if (s.endsWith("&")) { //s =
  		 * StringUtils.substringBeforeLast(s, "&"); s = s.substring(0,
  		 * s.lastIndexOf("&")); }
  		 */
  		return s;
  	}
  
  	public static void main(String[] args) {
  		Map<String, Object> map = new HashMap<String, Object>();
  		map.put("age", "20");
  		map.put("sex", "man");
  		map.put("name", "zhangsan");
  		System.out.println(getUrlParamsByMap(map, false));
  
  	}
}
