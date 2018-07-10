package cn.pay.loan.config;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMQConfig {

	public static final String BID_QUEUE = "bid.queue";
	public static final String LOGIN_QUEUE = "login.queue";

	/**
	 * 投标队列
	 * 
	 * @return
	 */
	@Bean
	public Queue bidQueue() {
		return new ActiveMQQueue(BID_QUEUE);
	}

	/**
	 * 登录队列
	 * @return
	 */
	@Bean
	public Queue loginQueue() {
		return new ActiveMQQueue(LOGIN_QUEUE);
	}

}
