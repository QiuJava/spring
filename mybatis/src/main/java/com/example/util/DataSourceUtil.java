package com.example.util;

/**
 * 数据源工具类
 * 
 * @author Qiu Jian
 * 
 */
public class DataSourceUtil {
	private DataSourceUtil() {
	}

	public static final String MASTER_DATASOURCE_KEY = "master";
	public static final String SLAVE_ONE_DATASOURCE_KEY = "slaveOne";

	private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

	public static void setKey(String key) {
		THREAD_LOCAL.set(key);
	}

	public static String getKey() {
		return THREAD_LOCAL.get();
	}

	public static void remove() {
		THREAD_LOCAL.remove();
	}
	
}
