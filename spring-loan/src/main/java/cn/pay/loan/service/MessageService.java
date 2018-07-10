package cn.pay.loan.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import cn.pay.core.service.BorrowService;
import cn.pay.core.util.StringUtil;
import cn.pay.loan.config.ActiveMQConfig;

/**
 * 消息队列相关服务
 * 
 * @author Qiujian
 *
 */
@Service
public class MessageService {

	@Autowired
	private BorrowService borrowService;
	
	Integer i = 100;

	@JmsListener(destination = ActiveMQConfig.BID_QUEUE)
	public void bid(String msg) {

		Map<String, String> map = StringUtil.mapStringToMap(msg);
		borrowService.bid(Long.valueOf(map.get("borrowId")), new BigDecimal(map.get("amount")),
				Long.valueOf(map.get("loginInfoId")));
	}

	@JmsListener(destination = ActiveMQConfig.LOGIN_QUEUE)
	public void login(String msg) {
		
		if (i > 0) {
			i--;
			System.out.println("商品还剩:" + i + msg);
		}
	}

}
