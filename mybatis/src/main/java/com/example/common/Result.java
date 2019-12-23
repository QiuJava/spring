package com.example.common;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Result<T> {

	private Boolean succeed;
	private String msg;
	private T data;

	public Result(Boolean succeed, String msg) {
		this.succeed = succeed;
		this.msg = msg;
	}

}
