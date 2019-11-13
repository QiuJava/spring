package com.example.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 员工锁相关数据
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class EmployeeLockDto {
	private Long id;
	private Date lockTime;
	private Integer passwordErrors;
	private Integer status;
	private Date updateTime;

}
