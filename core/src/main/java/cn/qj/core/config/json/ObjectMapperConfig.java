package cn.qj.core.config.json;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.qj.core.util.DateUtil;

/**
 * 使用Jackson作为系统的json处理工具
 * 
 * @author Qiujian
 * @date 2018/09/27
 */
@Configuration
public class ObjectMapperConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT_PATTERN));
		return objectMapper;
	}
}
