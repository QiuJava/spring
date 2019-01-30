package cn.qj.key.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 回复微信消息
 * 
 * @author Qiujian
 * @date 2018/12/20
 */
@Setter
@Getter
@ToString
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WechatReplyMsg {
	
	@XmlTransient
	private Long id;

	// 接收方帐号（收到的OpenID）
	@XmlElement(name = "ToUserName")
	private String toUserName;
	// 开发者微信号
	@XmlElement(name = "FromUserName")
	private String fromUserName;
	// 消息创建时间 （整型）
	@XmlElement(name = "CreateTime")
	private long createTime;
	// 消息类型
	@XmlElement(name = "MsgType")
	private String msgType;
	// 文本消息内容;
	@XmlElement(name = "Content")
	private String content;

	// 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
	@XmlElementWrapper(name = "Image")
	@XmlElement(name = "MediaId")
	private String[] mediaId;
	
	// 图文消息个数
	@XmlElement(name = "ArticleCount")
	private int articleCount;
	@XmlElementWrapper(name = "Articles")
	private WechatArticle[] item;

}
