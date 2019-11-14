package com.example.config.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.config.interceptor.RequestLogHandlerInterceptor;

/**
 * mvc自定义
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private RequestLogHandlerInterceptor requestLogHandlerInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestLogHandlerInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
	}
}
