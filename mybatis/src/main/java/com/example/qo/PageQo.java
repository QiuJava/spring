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
public class PageQo {
	private Integer page;
	private Integer rows;
	private Boolean count;

	public boolean getCount() {
		return count == null ? true : count;
	}
}
