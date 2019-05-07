package cn.qj.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 自定义mvc
 * 
 * @author Qiujian
 * @date 2019年3月27日
 *
 */
@Configuration
public class WebMvcConfigurerAdapterImpl extends WebMvcConfigurerAdapter {

	/**
	 * url 后缀精确匹配
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}

}
