package cn.qj.key.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

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
@XmlRootElement(name = "xml") // 指定传过来的xml文件根元素
@XmlAccessorType(XmlAccessType.FIELD) // 根据字段进行解析
public class WechatReplyMsg {

	private String ToUserName;// 用户的openId
	private String FromUserName;// 测试号
	private Long CreateTime;// 消息创建时间 （整型）
	private String MsgType;//
	private String Content; // 文本消息内容;

	@XmlElementWrapper(name = "Image")
	private String[] MediaId;// 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。

	private Integer ArticleCount;// 图文消息个数；当用户发送文本、图片、视频、图文、地理位置这五种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
	@XmlElementWrapper(name = "Articles")
	private ArticlesItem[] item;

}
