package com.example.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.example.util.SecurityContextUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 请求日志拦截处理
 * 
 * @author Qiu Jian
 */
@Configuration
@Slf4j
public class RequestLogHandlerInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			String className = handlerMethod.getBeanType().getName();
			String methodName = handlerMethod.getMethod().getName();
			
			StringBuilder builder = new StringBuilder();
			builder.append(className).append(".").append(methodName);
			
			log.info("请求者：{}，请求路径：{}，请求方法：{}，请求参数：{}", SecurityContextUtil.getCurrentEmployeeVo().getUsername(),
					request.getRequestURI(), builder.toString(), JSON.toJSONString(request.getParameterMap()));
		}
		return true;
	}

}
