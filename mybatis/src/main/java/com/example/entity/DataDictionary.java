package com.example.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据字典
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class DataDictionary implements Serializable {
	private static final long serialVersionUID = 751343883299580594L;
	public static final String EMPLOYEE_TYPE = "EMPLOYEE_TYPE";
	public static final String GENDER = "GENDER";
	public static final String EMPLOYEE_STATUS = "EMPLOYEE_STATUS";
	public static final String EMPLOYEE_DYNAMIC = "EMPLOYEE_DYNAMIC";

	private Integer id;
	private String dataKey;
	private String dataName;
	private String dataValue;
	private Date createTime;
	private Date updateTime;

}