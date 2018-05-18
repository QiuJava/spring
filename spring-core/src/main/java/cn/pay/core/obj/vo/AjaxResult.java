package cn.pay.core.obj.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ajax返回结果对象
 * 
 * @author Administrator
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResult {
	private boolean success;
	private String msg;
	private Integer errCode;

	public AjaxResult(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public AjaxResult(String msg) {
		this.msg = msg;
	}
}
