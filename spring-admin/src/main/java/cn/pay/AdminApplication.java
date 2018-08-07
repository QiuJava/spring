package cn.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.pay.admin.web.filter.XssFilter;
import cn.pay.core.consts.SysConst;

/**
 * 后台管理系统应用配置
 * 
 * @author Qiujian
 */
/**
 * @SpringBootApplication 注解 1.默认把启动类所在的包作为基础包 Spring中的bean扫描基础包和其子包 2.默认开启自动配置
 */
@SpringBootApplication
/**
 * @Profile 注解 标明当前的环境
 */
@Profile("dev")
/**
 * WebMvcConfigurerAdapter。class 配置WebMvc
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
		registration.setFilter(new XssFilter());// 添加过滤器
		registration.addUrlPatterns(SysConst.URL_ALL);// 设置过滤路径
		registration.setName("xssFilter");
		registration.setOrder(1);// 设置优先级
		return registration;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AdminApplication.class, args);
	}

}
