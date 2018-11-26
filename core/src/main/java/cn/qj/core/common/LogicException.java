package cn.qj.core.common;

import lombok.Getter;

/**
 * 系统逻辑异常
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
public class LogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@Getter
	private Integer errCode;

	public LogicException(String message) {
		super(message);
	}

	public LogicException(String message, Integer errCode) {
		super(message);
		this.errCode = errCode;
	}

}
