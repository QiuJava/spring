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

	private static final ThreadLocal<String> THREADLOCAL = new ThreadLocal<>();

	public static void setKey(String key) {
		THREADLOCAL.set(key);
	}

	public static String getKey() {
		return THREADLOCAL.get();
	}

	public static void remove() {
		THREADLOCAL.remove();
	}
	
}
