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

	public Boolean getCount() {
		return count == null ? true : count;
	}
	
	public void exportInit() {
		this.count = false;
		this.page = 1;
		this.rows = 0;
	}
}
