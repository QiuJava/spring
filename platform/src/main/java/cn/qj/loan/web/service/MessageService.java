package cn.qj.loan.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.qj.core.pojo.dto.BidDto;
import cn.qj.core.service.BorrowService;
import cn.qj.loan.config.mq.ActivemqConfig;

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

	@JmsListener(destination = ActivemqConfig.BID_QUEUE)
	public void bid(String msg) {
		BidDto dto = JSON.parseObject(msg, BidDto.class);
		borrowService.bid(dto.getBorrowId(), dto.getAmount(), dto.getLoginInfoId());
	}

	@JmsListener(destination = ActivemqConfig.LOGIN_QUEUE)
	public void login(String msg) {

	}

}
