package cn.eeepay.framework.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.jms.support.converter.MessageConverter;

import cn.eeepay.framework.model.bill.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ResponseQueueListener implements MessageListener {
	private static final Logger log = LoggerFactory.getLogger(ResponseQueueListener.class);
	private MessageConverter messageConverter;
	
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				System.out.println("接收到发送到responseQueue的一个文本消息，内容是：" + textMessage.getText());
			} catch (JMSException e) {
				log.error("异常:",e);
			}
		}else if (message instanceof ObjectMessage) {
			ObjectMessage objMessage = (ObjectMessage) message;
			try {
				Object obj = objMessage.getObject();
				Email email = (Email) obj;
				//Email email = (Email) messageConverter.fromMessage(objMessage);
				System.out.println("接收到一个ObjectMessage，包含Email对象。abcef");
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
