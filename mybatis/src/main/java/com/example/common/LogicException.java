package com.example.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 逻辑异常
 * 
 * @author Qiu Jian
 * 
 */
@NoArgsConstructor
@Getter
public class LogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private Integer errCode;

	public LogicException(String message) {
		super(message);
	}

	public LogicException(String message, Integer errCode) {
		super(message);
		this.errCode = errCode;
	}

}
