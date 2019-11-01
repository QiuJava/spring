package com.example.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 结果
 *
 * @author Qiu Jian
 *
 */
@Getter
@Setter
public class Result {

	private boolean succeed;
	private Integer statusCode;
	private String msg;
	private Object data;
}
