package cn.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.pay.admin.web.filter.XssFilter;
import cn.pay.admin.web.interceptor.UrlDoInterceptor;


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
	 * 注册自定义过滤器
	 * 
	 * @return
	 */
	@Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XssFilter());//添加过滤器
        registration.addUrlPatterns("*.do");//设置过滤路径
        registration.setName("XssFilter");
        registration.setOrder(1);//设置优先级
        return registration;
    }
	
	/*@Bean
	public ServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean bean = new ServletRegistrationBean();
		bean.getUrlMappings().clear();
		bean.setServlet(dispatcherServlet);
		bean.addUrlMappings("*.jpg", "*.png", "*.css", "*.js", "*.html","*.do","*.dlt","/error");
		return bean;
	}*/

	/**
	 * 
	 * 由ServletRegistrationBean去注册CoreServlet
	 * 配置CoreServlet  处理已.do结尾的请求
	 * 
	 * @param coreServlet
	 * @return
	 */
	/*@Bean
	public ServletRegistrationBean coreServletRegistration() {
		ServletRegistrationBean bean = new ServletRegistrationBean();
		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
		webContext.setParent(ac);
		DispatcherServlet ds = new DispatcherServlet(webContext);
		bean.setName("coreServlet");
		bean.addUrlMappings("*.do");
		bean.setServlet(ds);
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
	
	@Bean
	public HandlerInterceptor urlDoInterceptor() {
		return new UrlDoInterceptor();
	}
	
	/**
	 * 用户访问url是否需要登录拦截
	 * 
	 * @return
	 */
	/*@Bean
	public HandlerInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}*/

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
