package cn.pay.core.pojo.event;

import org.springframework.context.ApplicationEvent;

import cn.pay.core.entity.business.Borrow;
import lombok.Getter;

/**
 * 借款成功事件
 * 
 * @author Administrator
 *
 */
@Getter
public class BorrowEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private Borrow eventObj;

	public BorrowEvent(Object source, Borrow eventObj) {
		super(source);
		this.eventObj = eventObj;
	}

}
