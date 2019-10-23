package cn.eeepay.framework.util;

public class ThreadUtil {
	private ThreadUtil(){}

	private static final ThreadLocal<String> local = new ThreadLocal<>();
	public static void setParam(String str){
		local.set(str);
	}

	public static String getParam(){
		return local.get();
	}
	public static void removeThread() {
		local.remove();
	}
}
