package cn.qj.core.config.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * Druid视图配置
 * 
 * @author Qiujian
 * @date 2018/09/25
 */
@Configuration
public class DruidViewConfig {

	@Autowired
	private StatViewServletConfig statViewServletConfig;

	@Bean
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		servletRegistrationBean.addInitParameter("allow", statViewServletConfig.getAllow());
		servletRegistrationBean.addInitParameter("deny", statViewServletConfig.getDeny());
		servletRegistrationBean.addInitParameter("loginUsername", statViewServletConfig.getLoginUsername());
		servletRegistrationBean.addInitParameter("loginPassword", statViewServletConfig.getLoginPassword());
		servletRegistrationBean.addInitParameter("resetEnable",
				statViewServletConfig.getResetEnable().toString());
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean webStatFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.my,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

}
