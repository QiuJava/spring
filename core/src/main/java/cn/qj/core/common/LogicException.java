package cn.qj.core.common;

import lombok.NoArgsConstructor;

/**
 * 系统逻辑异常
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@NoArgsConstructor
public class LogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LogicException(String message) {
		super(message);
	}

}
