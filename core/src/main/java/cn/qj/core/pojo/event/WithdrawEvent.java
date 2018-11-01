package cn.qj.core.pojo.event;

import org.springframework.context.ApplicationEvent;

import cn.qj.core.entity.Withdraw;
import lombok.Getter;

/**
 * 提现成功事件
 * 
 * @author Qiujian
 * @date 2018/11/01
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
