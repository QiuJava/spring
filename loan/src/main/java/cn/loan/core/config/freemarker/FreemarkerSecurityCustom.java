package cn.loan.core.config.freemarker;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.jsp.TaglibFactory;

/**
 * Freemarker使用Security标签
 * 
 * @author Qiujian
 *
 */
@Configuration
public class FreemarkerSecurityCustom {
	
	@Autowired
	private FreeMarkerConfigurer configurer;

	/**
	 * @PostConstruct 注解 所贴的方法在配置类进行创建初始化之后就会执行
	 */
	@PostConstruct
	public void freeMarkerConfigurer() {
		List<String> tlds = new ArrayList<String>();
		tlds.add("/static/tags/security.tld");
		TaglibFactory factory = configurer.getTaglibFactory();
		factory.setClasspathTlds(tlds);
		if (factory.getObjectWrapper() == null) {
			factory.setObjectWrapper(configurer.getConfiguration().getObjectWrapper());
		}
	}

}
