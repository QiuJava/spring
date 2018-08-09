package cn.pay.core.pojo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 操作日志aop
 * 
 * @author Qiujian
 *
 */
@Component
@Aspect
public class OperationLogAop {

	@After("execution(* cn.pay..web..*(..)) && @annotation(requestMapping)")
	public void addOperationLog(JoinPoint joinPoint, RequestMapping requestMapping) {
		// 获取此次请求
		//HttpServletRequest request = HttpServletContext.getHttpServletRequest();
		//String servletPath = request.getServletPath();
	}

}