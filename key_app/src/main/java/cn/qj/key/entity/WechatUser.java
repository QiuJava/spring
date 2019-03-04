package cn.qj.key.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信用户
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
@Getter
@Setter
@ToString
@Entity
public class WechatUser {
	public static final Integer SUBSCRIBE = 1;
	public static final Integer UN_SUBSCRIBE = -1;

	/**
	 * 关注者id
	 */
	@Id
	private String openId;

	/**
	 * 关注时间
	 */
	private Date createTime;

	/**
	 * 1 为订阅 -1取消订阅
	 */
	private Integer subscribeType;

	/**
	 * 更新时间
	 */
	private Date updateTime;

}
