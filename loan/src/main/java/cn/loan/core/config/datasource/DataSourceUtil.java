package cn.loan.core.config.datasource;

/**
 * 数据源工具类
 * 
 * @author Qiujian
 * 
 */
public class DataSourceUtil {
	private DataSourceUtil() {
	}

	public static final String READ_ONE_KEY = "read_one";
	public static final String WRITE_KEY = "write";

	private static final ThreadLocal<String> THREADLOCAL = new ThreadLocal<>();

	public static void setDataSourceKey(String key) {
		THREADLOCAL.set(key);
	}

	public static String getDataSourceKey() {
		return THREADLOCAL.get();
	}

	public static void removeThreadLocal() {
		THREADLOCAL.remove();
	}
}
