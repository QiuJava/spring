package cn.qj.key.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 逻辑异常
 * 
 * @author Qiujian
 * @date 2018/12/18
 */
@NoArgsConstructor
public class LogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Integer errCode;

	public LogicException(Integer errCode, String errMsg) {
		super(errMsg);
		this.errCode = errCode;
	}

	public LogicException(String errMsg) {
		super(errMsg);
	}
}
