package cn.qj.key.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文章明细
 * 
 * @author Qiujian
 * @date 2018/12/20
 */
@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD) // 根据字段进行解析
public class ArticlesItem {

	private String Title;// 图文消息标题
	private String Description;// 图文消息描述
	private String PicUrl;// 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	private String Url;// 点击图文消息跳转链接

}
