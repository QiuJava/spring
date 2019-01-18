package cn.qj.key.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信接收消息
 * 
 * @author Qiujian
 * @date 2018/12/20
 */
@Setter
@Getter
@ToString
@XmlRootElement(name = "xml") // 指定传过来的xml文件根元素
@XmlAccessorType(XmlAccessType.FIELD) // 根据字段进行解析
public class WechatAcceptMsg {

	private String ToUserName;// 开发者微信号
	private String FromUserName;// 发送方帐号（一个OpenID）
	private Long CreateTime;// 消息创建时间 （整型）
	private String MsgType;//
	private Long MsgId;// 消息id，64位整型

	private String Content; // 文本消息内容;
	private String PicUrl;// 图片链接（由系统生成）
	private String MediaId;// 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。

	private String Event;// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)

	private String EventKey;//

}
