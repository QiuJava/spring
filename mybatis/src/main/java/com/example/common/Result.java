package com.example.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 结果
 *
 * @author Qiu Jian
 *
 */
@Getter
@Setter
@ToString
public class Result {

	private boolean succeed;
	private String msg;
	private Integer statusCode;
	private Object data;

	public Result() {
	}

	public Result(boolean succeed) {
		this.succeed = succeed;
	}

	public Result(boolean succeed, String msg) {
		this.succeed = succeed;
		this.msg = msg;
	}

	public Result(boolean succeed, String msg, Integer statusCode) {
		this.succeed = succeed;
		this.msg = msg;
		this.statusCode = statusCode;
	}

	public Result(boolean succeed, String msg, Integer statusCode, Object data) {
		this.succeed = succeed;
		this.msg = msg;
		this.statusCode = statusCode;
		this.data = data;
	}
}
