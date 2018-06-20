package cn.pay.loan;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfig {

	/**
	 * 投标队列
	 * 
	 * @return
	 */
	@Bean
	public Queue bidQueue() {
		return new ActiveMQQueue("bid.queue");
	}
}
