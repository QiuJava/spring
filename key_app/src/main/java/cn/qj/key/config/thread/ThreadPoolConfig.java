package cn.qj.key.config.thread;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 * 
 * @author Qiujian
 * @date 2019/01/18
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

	@Bean
	public ThreadPoolTaskExecutor threadPool() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8);
		executor.setMaxPoolSize(16);
		executor.setQueueCapacity(100);
		executor.setKeepAliveSeconds(60);
		executor.setThreadNamePrefix("threadPool");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

}
