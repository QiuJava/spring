package cn.pay.core.pojo.event;

import org.springframework.context.ApplicationEvent;

import cn.pay.core.entity.business.PaymentPlan;
import lombok.Getter;

/**
 * 收款成功事件
 * 
 * @author Qiu Jian
 *
 */
@Getter
public class PaymentPlanEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private PaymentPlan eventObj;

	public PaymentPlanEvent(Object source, PaymentPlan eventObj) {
		super(source);
		this.eventObj = eventObj;
	}

}
