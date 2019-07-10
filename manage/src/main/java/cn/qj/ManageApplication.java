package cn.qj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 后台管理系统配置启动类
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
@SpringBootApplication
@EnableJpaAuditing
@Profile("dev")
public class ManageApplication extends WebMvcConfigurerAdapter {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ManageApplication.class, args);
	}

}
