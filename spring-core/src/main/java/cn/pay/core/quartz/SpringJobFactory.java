package cn.pay.core.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * Spring管理Job工厂类
 * 
 * @author Administrator
 *
 */
@Component
public class SpringJobFactory extends AdaptableJobFactory {

	/**
	 * 注入可延伸的Spring工厂类
	 */
	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		// 调用父类的方法 quartzJob工厂中Job
		Object jobInstance = super.createJobInstance(bundle);
		// 进行注入到spring容器
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}
