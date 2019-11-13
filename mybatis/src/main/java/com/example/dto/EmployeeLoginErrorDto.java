package com.example.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 员工登录错误相关数据
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class EmployeeLoginErrorDto {
	private Long id;
	private Integer passwordErrors;
	private Date updateTime;
}
