package com.example.qo;

import com.example.common.Result;

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
public class BaseQo {
	private Integer pageNum;
	private Integer pageSize;
	private Boolean count;

	public Result verify() {
		if (this.count == null) {
			return new Result(false, "是否统计不能为空");
		}
		if (this.pageNum == null) {
			return new Result(false, "页数不能为空");
		}
		if (this.pageSize == null) {
			return new Result(false, "一页条数不能为空");
		}
		return null;
	}
}
