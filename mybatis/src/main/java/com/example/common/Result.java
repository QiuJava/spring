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

	private Boolean succeed;
	private String msg;
	private Integer statusCode;
	private Object data;

	public Result() {
	}

	public Result(Boolean succeed) {
		this.succeed = succeed;
	}

	public Result(Boolean succeed, String msg) {
		this.succeed = succeed;
		this.msg = msg;
	}

	public Result(Boolean succeed, String msg, Integer statusCode) {
		this.succeed = succeed;
		this.msg = msg;
		this.statusCode = statusCode;
	}

	public Result(Boolean succeed, String msg, Integer statusCode, Object data) {
		this.succeed = succeed;
		this.msg = msg;
		this.statusCode = statusCode;
		this.data = data;
	}
}
