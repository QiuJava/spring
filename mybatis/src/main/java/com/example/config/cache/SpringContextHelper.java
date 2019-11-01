
package com.example.config.cache;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文助手
 *
 * @author Qiu Jian
 *
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHelper.applicationContext = applicationContext;
	}

	public static <T> T getBean(Class<T> clz) {
		return applicationContext.getBean(clz);
	}

	public static <T> T getBean(String name, Class<T> clz) {
		return applicationContext.getBean(name, clz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

}
