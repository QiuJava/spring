package com.example.common;

import java.util.List;

import lombok.Getter;
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
public class PageResult<T> {
	private Long total;
	private List<T> rows;

	public PageResult(Long total, List<T> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

}
