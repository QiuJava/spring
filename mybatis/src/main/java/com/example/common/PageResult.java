package com.example.common;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 页结果
 * 
 * @author Qiu Jian
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
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
