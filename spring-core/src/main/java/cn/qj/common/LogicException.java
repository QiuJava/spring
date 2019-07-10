package cn.qj.common;

/**
 * 逻辑异常
 * 
 * @author qiujian@eeepay.cn
 *
 */
public class LogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LogicException(String errMsg) {
		super(errMsg);
	}

	public LogicException() {
		super();
	}

}
