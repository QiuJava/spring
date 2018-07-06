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

import cn.pay.loan.web.filter.XssFilter;
import cn.pay.loan.web.interceptor.UrlDoInterceptor;

/**
 * 借款网页应用相关配置
 * 
 * @author Administrator
 *
 */
@SpringBootApplication
//@ServletComponentScan
@EnableRedisHttpSession
@Profile("dev")
public class LoanApplication extends WebMvcConfigurerAdapter {
	
	@Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XssFilter());//添加过滤器
        registration.addUrlPatterns("*.do");//设置过滤路径
        registration.setName("XssFilter");
        registration.setOrder(1);//设置优先级
        return registration;
    }

	/**
	 * 设置SpringMvc处理的请求规则
	 * 
	 * @param dispatcherServlet
	 * @return
	 */
	/*@Bean
	public ServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean bean = new ServletRegistrationBean(dispatcherServlet);
		//bean.getUrlMappings().clear();
		return bean;
	}*/

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(urlDoInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

	/**
	 * 访问是否需要登录url拦截
	 * 
	 * @return
	 */
	@Bean
	public HandlerInterceptor urlDoInterceptor() {
		return new UrlDoInterceptor();
	}

	/**
	 * 属性资源解析器 
	 * 方法需要为静态 返回的Bean是容器级别
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
		SpringApplication.run(LoanApplication.class, args);
	}
	
}
