package cn.qj.key.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录日志
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Setter
@Getter
@Entity
public class LoginLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5628568607297544527L;
	
	/**
	 * 1：成功 2：失败
	 */
	public static final int SUCCESS_STATUS = 1;
	public static final int FAILURE_STATUS = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/**
	 * 登录用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private LoginUser loginUser;

	/**
	 * 登录状态
	 */
	private int status;

	/**
	 * 登录Ip
	 */
	private String ipAddress;

	/**
	 * 登录来源
	 */
	private int source;

	/**
	 * 登录时间
	 */
	private Date createTime;

}
