package com.example.entity;

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
public class LoginLog {

	public static final String LOGIN_SUCCESS_STATUS = "LOGIN_SUCCESS_STATUS";
	public static final String LOGIN_FAILURE_STATUS = "LOGIN_FAILURE_STATUS";

	private Long id;
	private String remoteAddress;
	private String username;
	private String loginStatus;
	
	private String remark;
	private Date createTime;
	private Date updateTime;

}