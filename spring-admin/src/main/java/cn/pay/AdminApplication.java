package cn.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 后台管理系统应用配置
 * 
 * @author Qiujian
 */
/**
 * @SpringBootApplication 注解
 * 1.默认把启动类所在的包作为基础包 Spring中的bean扫描基础包和其子包
 * 2.默认开启自动配置 
 */
@SpringBootApplication
/**
 * @ServletComponentScan 注解
 * 扫描Servlet组件  只有在嵌入servlet容器的情况下才起效果
 */
@ServletComponentScan
/**
 * @Profile 注解 标明当前的环境
 */
@Profile("dev")
/**
 * WebMvcConfigurerAdapter。class
 * 配置WebMvc
 *
 */
/**
 * @EnableTransactionManagement 开启事务管理 默认开启
 */
public class AdminApplication extends WebMvcConfigurerAdapter {

	/**
	 * 
	 * 由ServletRegistrationBean去注册DispatcherServlet
	 * 配置DispatcherServlet  处理什么样的请求
	 * 
	 * @param dispatcherServlet
	 * @return
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean bean = new ServletRegistrationBean(dispatcherServlet);
		bean.getUrlMappings().clear();
		bean.addUrlMappings("*.do");
		return bean;
	}

	/**
	 * 添加拦截器
	 */
	/*
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
	*/

	/**
	 * 用户访问url是否需要登录拦截
	 * 
	 * @return
	 */
	/*
	@Bean
	public HandlerInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}
	*/

	/**
	 * 属性资源解析器 
	 * 方法为静态 静态方法返回的bean是容器级别的
	 * 
	 * @return
	 */
	/*
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	*/

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AdminApplication.class, args);
	}

}
