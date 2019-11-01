package com.example.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基础查询对象
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class BaseQo  {
	private Integer pageNum;
	private Integer pageSize;
	private Boolean count;
}
