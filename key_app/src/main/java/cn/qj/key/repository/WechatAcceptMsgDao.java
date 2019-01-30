package cn.qj.key.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.key.entity.WechatAcceptMsg;

/**
 * 微信用户发送的消息数据操作
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
public interface WechatAcceptMsgDao extends JpaRepository<WechatAcceptMsg, String> {

	/**
	 * 根据消息id查询条数
	 * 
	 * @param msgId
	 * @return
	 */
	long countByMsgId(String msgId);

}
