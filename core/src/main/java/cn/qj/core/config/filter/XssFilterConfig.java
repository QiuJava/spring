package cn.qj.core.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.qj.core.common.XssFilter;

/**
 * Xss攻击过滤配置
 * 
 * @author Qiujian
 * @date 2018/11/19
 */
@Configuration
public class XssFilterConfig {

	@Bean
	public FilterRegistrationBean xssFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new XssFilter());
		bean.setName("xssFilter");
		return bean;
	}

}
