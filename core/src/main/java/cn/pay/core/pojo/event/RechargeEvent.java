package cn.pay.core.pojo.event;

import org.springframework.context.ApplicationEvent;

import cn.pay.core.entity.business.Recharge;
import lombok.Getter;

/**
 * 充值成功事件
 * 
 * @author Administrator
 *
 */
@Getter
public class RechargeEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private Recharge eventObj;

	public RechargeEvent(Object source, Recharge eventObj) {
		super(source);
		this.eventObj = eventObj;
	}

}