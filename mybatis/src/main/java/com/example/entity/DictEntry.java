package com.example.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典条目
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class DictEntry {
	public static final String EMPLOYEE_TYPE = "EMPLOYEE_TYPE";

	private Long id;
	private String name;
	private String value;
	private String dictKey;
	private Date createTime;
	private Date updateTime;

}
