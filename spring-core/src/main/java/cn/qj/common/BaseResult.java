package cn.qj.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础返回
 * 
 * @author qiujian@eeepay.cn
 *
 */
@Getter
@Setter
public class BaseResult {
	private boolean success;
	private String msg;
	private Object data;

	public BaseResult(boolean success, String msg, Object data) {
		super();
		this.success = success;
		this.msg = msg;
		this.data = data;
	}

	public static BaseResult ok(String msg, Object data) {
		return new BaseResult(Boolean.TRUE, msg, data);
	}

	public static BaseResult err(String msg, Object data) {
		return new BaseResult(Boolean.FALSE, msg, data);
	}

}
