package com.example.config.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.listener.ContextStartListener;

/**
 * 监听器配置
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class ListenerConfig {

	@Bean
	public ContextStartListener contextStartListener() {
		return new ContextStartListener();
	}

}
