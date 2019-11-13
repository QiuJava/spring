package com.example.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 重置密码模型
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class ResetPasswordModel {
	private String username;
	private String employeeNumber;
	private String nickname;
	private String email;
	private String password;
	private Date updateTime;
}
