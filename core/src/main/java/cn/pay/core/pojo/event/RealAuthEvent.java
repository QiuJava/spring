package cn.pay.core.pojo.event;

import org.springframework.context.ApplicationEvent;

import cn.pay.core.domain.business.RealAuth;
import lombok.Getter;

/**
 * 实名认证成功事件
 * 
 * @author Administrator
 *
 */
@Getter
public class RealAuthEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private RealAuth eventObj;

	public RealAuthEvent(Object source, RealAuth eventObj) {
		super(source);
		this.eventObj = eventObj;
	}

}
