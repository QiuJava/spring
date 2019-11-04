package com.example.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

/**
 * 员工
 *
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class Employee implements Serializable {
	private Long id;
	private String username;
	private String password;
	private String email;
	private String nickname;
	private Integer passwordErrors;
	private Integer status;
	private Integer superAdmin;
	private Integer employeeType;
	private String employeeNumber;
	private String intro;
	private Date createTime;
	private Date updateTime;

	public static final int NORMAL_STATUS = 0;
	public static final int LOCK_STATUS = 1;
	public static final int INVALID_STATUS = 2;

	public static final int IS_ADMIN = 1;
	public static final int IS_NOT_ADMIN = 0;

	public static final int ADMIN_TYPE = 0;

	private static final long serialVersionUID = 1221985552224614692L;

}