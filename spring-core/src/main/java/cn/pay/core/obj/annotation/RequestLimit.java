package cn.pay.core.obj.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * controller url请求限制注解
 * 
 * @author Qiujian
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit {
	/**
	 * 允许访问的最大次数
	 */
	int count() default Integer.MAX_VALUE;

	/**
	 * 时间段，单位为毫秒，默认值一分钟
	 */
	long time() default 60000;
}
