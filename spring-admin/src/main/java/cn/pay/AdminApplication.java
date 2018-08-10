package cn.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.pay.admin.web.filter.XssFilter;
import cn.pay.core.consts.SysConst;

/**
 * 后台管理系统配置启动类
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@SpringBootApplication
@Profile("dev")
public class AdminApplication extends WebMvcConfigurerAdapter {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}

	/**
	 * 注册自定义过滤器
	 * 
	 * @return
	 */
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
		SpringApplication.run(AdminApplication.class, args);
	}

}
