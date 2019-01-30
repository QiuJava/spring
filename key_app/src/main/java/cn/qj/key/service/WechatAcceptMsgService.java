package cn.qj.key.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qj.key.entity.WechatAcceptMsg;
import cn.qj.key.repository.WechatAcceptMsgDao;

/**
 * 微信用户发送的消息服务
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
@Service
public class WechatAcceptMsgService {

	@Autowired
	private WechatAcceptMsgDao dao;

	public long countByMsgId(String msgId) {
		return dao.countByMsgId(msgId);
	}

	public void save(WechatAcceptMsg msg) {
		dao.save(msg);
	}

}
