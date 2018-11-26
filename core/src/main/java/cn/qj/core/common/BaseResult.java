package cn.qj.core.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础请求返回
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Data
@NoArgsConstructor
public class BaseResult {

	private boolean success;
	private String msg;
	private Integer statusCode;
	private Object data;

	public BaseResult(boolean success, String msg, Integer statusCode) {
		super();
		this.success = success;
		this.msg = msg;
		this.statusCode = statusCode;
	}

	public BaseResult(boolean success, String msg, Integer statusCode, Object data) {
		super();
		this.success = success;
		this.msg = msg;
		this.statusCode = statusCode;
		this.data = data;
	}

}
