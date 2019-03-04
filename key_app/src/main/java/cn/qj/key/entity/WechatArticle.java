package cn.qj.key.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

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
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class WechatArticle {

	public static final Integer VALID = 1;
	public static final Integer INVALID = -1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlTransient
	private Long id;

	/**
	 * 图文消息标题
	 */
	@XmlElement(name = "Title")
	private String title;

	/**
	 * 图文消息描述
	 */
	@XmlElement(name = "Description")
	private String description;

	/**
	 * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	 */
	@XmlElement(name = "PicUrl")
	private String picUrl;

	/**
	 * 点击图文消息跳转链接
	 */
	@XmlElement(name = "Url")
	private String url;

	@XmlTransient
	private Date createTime;

	@XmlTransient
	private Date updateTime;

	@XmlTransient
	private Integer status;

}
