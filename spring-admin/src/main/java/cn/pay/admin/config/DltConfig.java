package cn.pay.admin.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.jsp.TaglibFactory;

/**
 * 给模板注入安全标签
 * 
 * @author Qiujian
 * @date 2018年8月13日
 */
@Configuration
public class DltConfig {

	private static final String SPIRNG_SECURITY_TAGS_TLD_STATIC_PATH = "/static/tags/security.tld";

	@Autowired
	private FreeMarkerConfigurer configurer;

	/**
	 * @PostConstruct 注解 所贴的方法在配置类进行创建初始化之后就会执行
	 */
	@PostConstruct
	public void freeMarkerConfigurer() {
		List<String> tlds = new ArrayList<String>();
		tlds.add(SPIRNG_SECURITY_TAGS_TLD_STATIC_PATH);
		TaglibFactory taglibFactory = configurer.getTaglibFactory();
		taglibFactory.setClasspathTlds(tlds);
		if (taglibFactory.getObjectWrapper() == null) {
			taglibFactory.setObjectWrapper(configurer.getConfiguration().getObjectWrapper());
		}
	}
}
