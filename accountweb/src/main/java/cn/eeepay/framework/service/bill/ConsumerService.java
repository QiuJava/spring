package cn.eeepay.framework.service.bill;

import javax.jms.JMSException;

public interface ConsumerService {

	public void receiveMessage(String message) throws JMSException;
	
}
