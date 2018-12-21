package cn.qj.key;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan("cn.qj.key.mapper")
public class KeyAppApplication {
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@Bean
	public RestTemplate restTemplate() {
		return restTemplateBuilder.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(KeyAppApplication.class, args);
	}

}

