package cn.qj.key.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import cn.qj.key.config.datasource.DataSourceKey;
import cn.qj.key.config.datasource.DataSourceUtil;

/**
 * 数据源选择aop
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Component
@Aspect
public class DataSourceAop {

	@Around("@annotation(dataSourceKey)")
	public Object around(ProceedingJoinPoint joinPoint, DataSourceKey dataSourceKey) {
		Object proceed = null;
		try {
			DataSourceUtil.setDataSourceKey(dataSourceKey.value());
			proceed = joinPoint.proceed();
			return proceed;
		} catch (Throwable e) {
			e.printStackTrace();
			return proceed;
		} finally {
			DataSourceUtil.removeThreadLocal();
		}
	}

}