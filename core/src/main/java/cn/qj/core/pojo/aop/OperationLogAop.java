package cn.qj.core.pojo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 操作日志切面
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Component
@Aspect
public class OperationLogAop {

	@After("execution(* cn.qj..web..*(..)) && @annotation(requestMapping)")
	public void addOperationLog(JoinPoint joinPoint, RequestMapping requestMapping) {
		// 获取此次请求
		// HttpServletRequest request = HttpServletContext.getHttpServletRequest();
		// String servletPath = request.getServletPath();
	}

}