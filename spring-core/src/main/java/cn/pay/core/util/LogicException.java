package cn.pay.core.util;

/**
 * 逻辑异常类
 * 
 * @author Qiujian
 *
 */
public class LogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private Integer errCode;

	public Integer getErrCode() {
		return errCode;
	}

	public LogicException(String message) {
		super(message);
	}

	public LogicException(String message, Integer errCode) {
		super(message);
		this.errCode = errCode;
	}

}
