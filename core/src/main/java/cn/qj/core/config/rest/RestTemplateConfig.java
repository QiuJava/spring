package cn.qj.core.config.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Rest模板配置
 * 
 * @author Qiujian
 * @date 2018/09/29
 */
@Configuration
public class RestTemplateConfig {

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@Bean
	public RestTemplate restTemplate() {
		return restTemplateBuilder.build();
	}
}
