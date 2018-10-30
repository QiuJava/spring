package cn.qj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 借款网页应用相关配置
 * 
 * @author Administrator
 *
 */
@SpringBootApplication
@EnableRedisHttpSession
@Profile("dev")
public class PlatformApplication extends WebMvcConfigurerAdapter {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(PlatformApplication.class, args);
	}

}
