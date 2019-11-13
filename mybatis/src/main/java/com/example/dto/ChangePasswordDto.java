package com.example.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 修改密码数据
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class ChangePasswordDto {

	private String username;
	private String encodePassword;
	private Date updateTime;

}
