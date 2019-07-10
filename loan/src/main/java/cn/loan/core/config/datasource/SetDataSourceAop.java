package cn.loan.core.config.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

/**
 * 设置数据源
 * 
 * @author qiujian
 *
 */
@Aspect
@Configuration
public class SetDataSourceAop {

	@Around("@annotation(dataSourceKey)")
	public Object aroundSetDataSource(ProceedingJoinPoint joinPoint, DataSourceKey dataSourceKey) {
		Object obj = null;
		try {
			DataSourceUtil.setDataSourceKey(dataSourceKey.value());
			obj = joinPoint.proceed();
			DataSourceUtil.removeThreadLocal();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return obj;
	}

}
