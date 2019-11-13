package com.example.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 员工
 *
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class Employee {
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
	private Date lockTime;
	private Date createTime;
	private Date updateTime;

	public static final int NORMAL_STATUS = 0;
	public static final int LOCK_STATUS = 1;
	public static final int INVALID_STATUS = 2;

	public static final int IS_ADMIN = 1;
	public static final int IS_NOT_ADMIN = 0;

	public static final int ADMIN_TYPE = 0;

	public static final int MAX_PASSWORD_ERRORS = 5;

	public static final int PASSWORD_ERRORS_INIT = 0;

	public static final String INIT_PASSWORD_SUFFIX = "a123";
	public static final String INIT_EMPLOYEE_NUMBER = "000";

}