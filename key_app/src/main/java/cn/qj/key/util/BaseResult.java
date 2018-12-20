package cn.qj.key.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 基本结果返回
 * 
 * @author Qiujian
 * @date 2018/12/17
 */
@Getter
@Setter
@NoArgsConstructor
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

	public BaseResult(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

}
