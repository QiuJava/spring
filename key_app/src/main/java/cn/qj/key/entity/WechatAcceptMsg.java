package cn.qj.key.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 接收消息
 * 
 * @author Qiujian
 * @date 2018/12/20
 */
@Setter
@Getter
@ToString
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class WechatAcceptMsg {

	/**
	 * 消息id，64位整型
	 */
	@Id
	@XmlElement(name = "MsgId")
	private String msgId;

	/**
	 * 开发者微信号
	 */
	@XmlElement(name = "ToUserName")
	private String toUserName;

	/**
	 * 发送方帐号（一个OpenID）
	 */
	@XmlElement(name = "FromUserName")
	private String fromUserName;

	/**
	 * 消息创建时间 （整型）
	 */
	@XmlElement(name = "CreateTime")
	private Long createTime;

	private Date createDateTime;

	/**
	 * 消息类型
	 */
	@XmlElement(name = "MsgType")
	private String msgType;

	/**
	 * 文本消息内容
	 */
	@XmlElement(name = "Content")
	private String content;

	/**
	 * 图片链接（由系统生成）
	 */
	@XmlElement(name = "PicUrl")
	private String picUrl;

	/**
	 * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
	 */
	@XmlElement(name = "MediaId")
	private String mediaId;

	/**
	 * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
	 */
	@XmlElement(name = "Event")
	private String event;

	@XmlElement(name = "EventKey")
	private String eventKey;

}
