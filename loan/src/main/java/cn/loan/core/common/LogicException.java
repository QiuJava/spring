package cn.loan.core.common;

import lombok.NoArgsConstructor;

/**
 * 逻辑异常
 * 
 * @author Qiujian
 * 
 */
@NoArgsConstructor
public class LogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LogicException(String message) {
		super(message);
	}

}
