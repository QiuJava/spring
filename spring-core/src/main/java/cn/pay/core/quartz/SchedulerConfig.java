package cn.pay.core.quartz;

import java.io.IOException;
import java.util.Properties;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时调度器配置类
 * 
 * @author Administrator
 *
 */
@Configuration
public class SchedulerConfig {

	@Autowired
	private SpringJobFactory springJobFactory;

	/**
	 * 配置调度器工厂Bean
	 * 
	 * @return
	 * @throws IOException
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setQuartzProperties(quartzProperties());
		factory.setJobFactory(springJobFactory);
		return factory;
	}

	/**
	 * 设置quartz属性对象
	 * 
	 * @return
	 * @throws IOException
	 */
	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	/**
	 * 根据调度器工厂Bean获取调度器
	 * 
	 * @return
	 * @throws IOException
	 */
	@Bean
	public Scheduler scheduler() throws IOException {
		return schedulerFactoryBean().getScheduler();
	}

}
