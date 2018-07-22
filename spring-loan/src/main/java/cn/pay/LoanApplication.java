package cn.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.pay.core.consts.SysConst;
import cn.pay.loan.web.filter.XssFilter;
import cn.pay.loan.web.interceptor.UrlDoInterceptor;

/**
 * 借款网页应用相关配置
 * 
 * @author Administrator
 *
 */
@SpringBootApplication
@EnableRedisHttpSession
@Profile("dev")
public class LoanApplication extends WebMvcConfigurerAdapter {

	@Bean
	public FilterRegistrationBean xssFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new XssFilter());// 添加过滤器
		registration.addUrlPatterns(SysConst.URL_SUFFIX_DO);// 设置过滤路径
		registration.setName("XssFilter");
		registration.setOrder(1);// 设置优先级
		return registration;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(urlDoInterceptor()).addPathPatterns(SysConst.URL_ALL);
		super.addInterceptors(registry);
	}

	@Bean
	public HandlerInterceptor urlDoInterceptor() {
		return new UrlDoInterceptor();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(LoanApplication.class, args);
	}

}
