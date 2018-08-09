package cn.pay.admin.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.pay.core.pojo.event.BorrowEvent;
import cn.pay.core.pojo.event.PaymentPlanEvent;
import cn.pay.core.pojo.event.RealAuthEvent;
import cn.pay.core.pojo.event.RechargeEvent;
import cn.pay.core.pojo.event.WithdrawEvent;
import cn.pay.core.service.SendSmsService;

/**
 * 相关业务审核成功事件监听
 * 
 * @author Administrator
 *
 */
@Component
public class AuditSuccessListener implements ApplicationListener<ApplicationEvent> {

	@Autowired
	private SendSmsService sendSmsService;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof BorrowEvent) {
			BorrowEvent borrowEvent = (BorrowEvent) event;
			sendSmsService.borrowSuccess(borrowEvent.getEventObj());
		} else if (event instanceof PaymentPlanEvent) {
			PaymentPlanEvent paymentPlanEvent = (PaymentPlanEvent) event;
			sendSmsService.paymentSuccess(paymentPlanEvent.getEventObj());
		} else if (event instanceof RealAuthEvent) {
			RealAuthEvent realAuthEvent = (RealAuthEvent) event;
			sendSmsService.realAuthSuccess(realAuthEvent.getEventObj());
		} else if (event instanceof RechargeEvent) {
			RechargeEvent rechargeEvent = (RechargeEvent) event;
			sendSmsService.rechargeSuccess(rechargeEvent.getEventObj());
		} else if (event instanceof WithdrawEvent) {
			WithdrawEvent withdrawEvent = (WithdrawEvent) event;
			sendSmsService.withdrawSuccess(withdrawEvent.getEventObj());
		}
	}

}
