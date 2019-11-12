package com.example.config.result;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.example.util.DateTimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义ObjectMapper 返回对象Json解析
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class ResultJsonConfig {

	@Primary
	@Bean
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper objectMapper = builder.createXmlMapper(false).build();
		objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.DATATIME_PATTERN));
		return objectMapper;
	}

}
