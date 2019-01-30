package cn.qj.key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.qj.key.config.interceptor.WechatAccessTokenInterceptor;

/**
 * 应用配置
 * 
 * @author Qiujian
 * @date 2019/01/17
 */
@SpringBootApplication
@ServletComponentScan
public class KeyAppApplication extends WebMvcConfigurerAdapter {

	@Autowired
	private WechatAccessTokenInterceptor wechatAccessTokenInterceptor;

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(wechatAccessTokenInterceptor).addPathPatterns("/wechat/*");
		
	}

	public static void main(String[] args) {
		SpringApplication.run(KeyAppApplication.class, args);
	}

}
