package cn.qj.admin.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.qj.core.pojo.event.BorrowEvent;
import cn.qj.core.pojo.event.PaymentPlanEvent;
import cn.qj.core.pojo.event.RealAuthEvent;
import cn.qj.core.pojo.event.RechargeEvent;
import cn.qj.core.pojo.event.WithdrawEvent;
import cn.qj.core.service.SendSmsService;

/**
 * 相关业务审核成功事件监听
 * 
 * @author Qiujian
 * @date 2018/10/30
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
		}

		if (event instanceof PaymentPlanEvent) {
			PaymentPlanEvent paymentPlanEvent = (PaymentPlanEvent) event;
			sendSmsService.paymentSuccess(paymentPlanEvent.getEventObj());
		}

		if (event instanceof RealAuthEvent) {
			RealAuthEvent realAuthEvent = (RealAuthEvent) event;
			sendSmsService.realAuthSuccess(realAuthEvent.getEventObj());
		}

		if (event instanceof RechargeEvent) {
			RechargeEvent rechargeEvent = (RechargeEvent) event;
			sendSmsService.rechargeSuccess(rechargeEvent.getEventObj());
		}

		if (event instanceof WithdrawEvent) {
			WithdrawEvent withdrawEvent = (WithdrawEvent) event;
			sendSmsService.withdrawSuccess(withdrawEvent.getEventObj());
		}
	}

}
