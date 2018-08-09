package cn.pay.core.pojo.event;

import org.springframework.context.ApplicationEvent;

import cn.pay.core.domain.business.Withdraw;
import lombok.Getter;

/**
 * 提现成功事件
 * 
 * @author Administrator
 *
 */
@Getter
public class WithdrawEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private Withdraw eventObj;

	public WithdrawEvent(Object source, Withdraw eventObj) {
		super(source);
		this.eventObj = eventObj;
	}

}
