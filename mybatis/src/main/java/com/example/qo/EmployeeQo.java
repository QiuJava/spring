package com.example.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 员工查询对象
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class EmployeeQo extends PageQo {

	private String employeeName;
	private String employeeType;
	
}
