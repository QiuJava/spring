package cn.qj.core.pojo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import cn.qj.core.util.DataSourceUtil;

/**
 * 操作日志切面
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Component
@Aspect
public class DataSourceAop {

	@Before("execution(* cn.qj.core.service.*.*(..))")
	public void around(JoinPoint joinPoint) {
		DataSourceUtil.setDataSourceKey(DataSourceUtil.READ_ONE_KEY);
	}

}