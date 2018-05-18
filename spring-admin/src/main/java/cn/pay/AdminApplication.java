package cn.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.pay.admin.web.interceptor.LoginInterceptor;

@SpringBootApplication
@ServletComponentScan
@Profile("dev")
public class AdminApplication extends WebMvcConfigurerAdapter {

	@Bean
	public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean bean = new ServletRegistrationBean(dispatcherServlet);
		bean.getUrlMappings().clear();
		// 设置DispatcherServlet拦截的请求
		bean.addUrlMappings("*.do");
		bean.addUrlMappings("*.json");
		return bean;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

	@Bean
	public HandlerInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AdminApplication.class, args);
	}

}
