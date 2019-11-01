package com.example.config.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.interceptor.MyHandlerInterceptor;

/**
 * 拦截器配置
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class InterceptorConfig {

	@Bean
	public MyHandlerInterceptor myHandlerInterceptor() {
		return new MyHandlerInterceptor();
	}

}
