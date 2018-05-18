package cn.pay.core.obj.event;

import org.springframework.context.ApplicationEvent;

import cn.pay.core.domain.business.PaymentPlan;
import lombok.Getter;

@Getter
public class PaymentPlanEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private PaymentPlan eventObj;

	public PaymentPlanEvent(Object source, PaymentPlan eventObj) {
		super(source);
		this.eventObj = eventObj;
	}

}
