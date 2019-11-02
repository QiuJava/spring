package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用启动
 *
 * @author Qiu Jian
 *
 */
@SpringBootApplication
@MapperScan("com.example.mapper")
@EnableRedisHttpSession
@ServletComponentScan("com.example.servlet")
@ComponentScan({ "com.example.service", "com.example.controller", "com.example.config" })
@EnableTransactionManagement
public class MybatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybatisApplication.class, args);
	}

}
