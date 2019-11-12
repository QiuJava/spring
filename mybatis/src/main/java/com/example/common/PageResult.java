package com.example.common;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 页
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
public class PageResult<T> {
	private Integer pageNum;
	private Integer pageSize;
	private Long total;
	private List<T> list;

	public PageResult(Integer pageNum, Integer pageSize, Long total, List<T> list) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.total = total;
		this.list = list;
	}

}
