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
public class Result<T> {

	private Boolean succeed;
	private String msg;
	private T data;

	public Result() {
	}

	public Result(Boolean succeed) {
		this.succeed = succeed;
	}

	public Result(Boolean succeed, String msg) {
		this.succeed = succeed;
		this.msg = msg;
	}

	public Result(Boolean succeed, String msg, T data) {
		this.succeed = succeed;
		this.msg = msg;
		this.data = data;
	}
}
