package cn.qj.core.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 基础请求返回
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BaseResult {

	private boolean success;
	private String msg;
	private int statusCode;
	private Object data;

	public BaseResult(boolean success, String msg, int statusCode) {
		super();
		this.success = success;
		this.msg = msg;
		this.statusCode = statusCode;
	}

	public BaseResult(boolean success, String msg, int statusCode, Object data) {
		super();
		this.success = success;
		this.msg = msg;
		this.statusCode = statusCode;
		this.data = data;
	}

	public static BaseResult err500() {
		return new BaseResult(false, "系统异常", 500);
	}

	public static BaseResult err400(String msg) {
		return new BaseResult(false, msg, 400);
	}

	public static BaseResult ok(String msg) {
		return new BaseResult(true, msg, 200);
	}
}
