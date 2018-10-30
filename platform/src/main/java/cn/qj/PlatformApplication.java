package cn.qj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.qj.core.consts.SysConst;
import cn.qj.loan.web.filter.XssFilter;

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

	@Bean
	public FilterRegistrationBean xssFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new XssFilter());
		registration.addUrlPatterns(SysConst.URL_ALL);
		registration.setName("xssFilter");
		registration.setOrder(1);
		return registration;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(PlatformApplication.class, args);
	}

}
