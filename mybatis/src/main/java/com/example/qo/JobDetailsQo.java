package com.example.qo;

import com.example.common.Result;

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

	public Result verify() {
		if (this.pageNum == null || pageNum < 1) {
			return new Result(false, "页数不正确");
		}
		if (this.pageSize == null || pageSize < 0) {
			return new Result(false, "一页条数不正确");
		}
		return null;
	}
}
