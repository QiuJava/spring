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

/**
 * 后台管理系统应用配置
 * 
 * @author Administrator
 *
 */
@SpringBootApplication
@ServletComponentScan
@Profile("dev")
public class AdminApplication extends WebMvcConfigurerAdapter {

	/**
	 * 设置DispatcherServlet拦截的请求
	 * 
	 * @param dispatcherServlet
	 * @return
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean bean = new ServletRegistrationBean(dispatcherServlet);
		bean.getUrlMappings().clear();

		bean.addUrlMappings("*.do");
		bean.addUrlMappings("*.json");
		return bean;
	}

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

	/**
	 * 用户访问url是否需要登录拦截
	 * 
	 * @return
	 */
	@Bean
	public HandlerInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}

	/**
	 * 属性资源解析器 方法需要为静态 容器级别
	 * 
	 * @return
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AdminApplication.class, args);
	}

}
