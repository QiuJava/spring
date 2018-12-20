package cn.qj.key;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.qj.key.mapper")
public class KeyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyAppApplication.class, args);
	}

}

