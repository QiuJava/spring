package cn.pay.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.pay.core.obj.dto.BidDto;
import cn.pay.core.service.BorrowService;
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

	@JmsListener(destination = ActiveMQConfig.BID_QUEUE)
	public void bid(String msg) {
		BidDto dto = JSON.parseObject(msg, BidDto.class);
		borrowService.bid(dto.getBorrowId(), dto.getAmount(), dto.getLoginInfoId());
	}

	@JmsListener(destination = ActiveMQConfig.LOGIN_QUEUE)
	public void login(String msg) {

	}

}
