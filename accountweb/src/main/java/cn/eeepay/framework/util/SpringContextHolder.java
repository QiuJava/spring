package cn.eeepay.framework.util;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext context;

	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return context;
	}

	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return context.getBean(clazz);
	}

	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		context = ac;
	}

	private static void checkApplicationContext() {
		if (context == null) {
			throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}
}
