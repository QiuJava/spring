package cn.qj.core.pojo.event;

import org.springframework.context.ApplicationEvent;

import cn.qj.core.entity.Recharge;
import lombok.Getter;

/**
 * 充值成功事件
 * 
 * @author Qiujian
 * @date 2018/11/01
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
