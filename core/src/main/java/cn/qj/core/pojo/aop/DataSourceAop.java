package cn.qj.core.pojo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import cn.qj.core.common.DataSourceKey;
import cn.qj.core.util.DataSourceUtil;

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
		// 获取方法签名
		// MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		// DataSourceKey dataSourceKey =
		// signature.getMethod().getAnnotation(DataSourceKey.class);
		DataSourceUtil.setDataSourceKey(dataSourceKey.value());

		Object proceed = null;
		try {
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