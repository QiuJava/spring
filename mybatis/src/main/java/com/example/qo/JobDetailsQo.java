package com.example.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 任务查询对象
 * 
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class JobDetailsQo {

	private String jobGroupName;
	private Integer pageNum;
	private Integer pageSize;

}
