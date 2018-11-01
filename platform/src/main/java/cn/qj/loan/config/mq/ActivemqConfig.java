package cn.qj.loan.config.mq;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mq配置
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
@Configuration
public class ActivemqConfig {

	public static final String BID_QUEUE = "bid.queue";
	public static final String LOGIN_QUEUE = "login.queue";
	public static final String LOGIN_INFO_QUEUE = "loginInfo.queue";
	public static final String IP_LOG_QUEUE = "ipLog.queue";

	@Bean
	public Queue loginInfoQueue() {
		return new ActiveMQQueue(LOGIN_INFO_QUEUE);
	}

	@Bean
	public Queue ipLogQueue() {
		return new ActiveMQQueue(IP_LOG_QUEUE);
	}

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
	 * 
	 * @return
	 */
	@Bean
	public Queue loginQueue() {
		return new ActiveMQQueue(LOGIN_QUEUE);
	}

}
