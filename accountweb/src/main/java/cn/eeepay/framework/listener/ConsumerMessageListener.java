package cn.eeepay.framework.listener;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.jms.support.converter.MessageConverter;

import cn.eeepay.framework.model.bill.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerMessageListener implements MessageListener {
	private static final Logger log = LoggerFactory.getLogger(ConsumerMessageListener.class);
	
	private MessageConverter messageConverter;
	
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			//这里我们知道生产者发送的就是一个纯文本消息，所以这里可以直接进行强制转换，或者直接把onMessage方法的参数改成Message的子类TextMessage
			TextMessage textMsg = (TextMessage) message;
			System.out.println("接收到一个纯文本消息。");
			try {
				System.out.println("消息内容是：" + textMsg.getText());
			} catch (JMSException e) {
				log.error("异常:",e);
			}
		} else if (message instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage) message;
		} else if (message instanceof ObjectMessage) {
			ObjectMessage objMessage = (ObjectMessage) message;
			try {
				/*Object obj = objMessage.getObject();
				Email email = (Email) obj;*/
				Email email = (Email) messageConverter.fromMessage(objMessage);
				email.setContent("onMessage:"+new Date());
				System.out.println("接收到一个ObjectMessage，包含Email对象11。");
				System.out.println(email);
			} catch (JMSException e) {
				log.error("异常:",e);
			}
			
		}
	}

	public MessageConverter getMessageConverter() {
		return messageConverter;
	}

	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

}
