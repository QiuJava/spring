package cn.loan.core.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * mvc 自定义
 * 
 * @author qiujian
 *
 */
@Configuration
public class WebMvcCustom extends WebMvcConfigurerAdapter {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		/**
		 * 请求地址精确匹配
		 */
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}

}
