package com.example.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 登录日志
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class LoginLog implements Serializable {
	private static final long serialVersionUID = 2535629589053325338L;

	public static final int LOGIN_SUCCESS_STATUS = 1;
	public static final int LOGIN_FAILURE_STATUS = 0;

	private Long id;
	private String remoteAddress;
	private String username;
	private Integer loginType;
	private String remark;
	private Date createTime;
	private Date updateTime;

}