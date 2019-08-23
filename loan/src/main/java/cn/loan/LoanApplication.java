package cn.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Profile;

/**
 * 应用启动
 * 
 * @author qiujian
 *
 */
@SpringBootApplication
@ServletComponentScan
@Profile("dev")
public class LoanApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanApplication.class, args);
	}

}
