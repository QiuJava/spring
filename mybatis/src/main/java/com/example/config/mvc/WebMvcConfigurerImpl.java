package com.example.config.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.interceptor.MyHandlerInterceptor;

/**
 * mvc自定义
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {

	@Autowired
	private MyHandlerInterceptor myHandlerInterceptor;

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(myHandlerInterceptor).addPathPatterns("/*");
	}
}
