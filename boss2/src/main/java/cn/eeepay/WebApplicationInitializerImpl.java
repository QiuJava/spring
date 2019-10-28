package cn.eeepay;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import cn.eeepay.boss.system.GlobalDispatcherServlet;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4JServletContextListener;

public class WebApplicationInitializerImpl implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// 初始化SpringConfig
		XmlWebApplicationContext xmlWebApplicationContext = new XmlWebApplicationContext();
		xmlWebApplicationContext.setConfigLocation("classpath*:applicationContext.xml");
		
		// 添加监听器
		servletContext.addListener(new ContextLoaderListener(xmlWebApplicationContext));
		servletContext.addListener(new SysOutOverSLF4JServletContextListener());
		servletContext.addListener(new HttpSessionEventPublisher());
		// 添加过滤器
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		servletContext.addFilter("encodingFilter", encodingFilter);
		servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);

		// 添加Servlet
		GlobalDispatcherServlet globalDispatcherServlet = new GlobalDispatcherServlet();
		globalDispatcherServlet.setApplicationContext(xmlWebApplicationContext);
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("mvc", globalDispatcherServlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		
	}

}
