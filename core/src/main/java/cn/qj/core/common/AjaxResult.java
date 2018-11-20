package cn.qj.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ajax请求返回
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResult {

	private boolean success;
	private String msg;
	private Integer errCode;
	
	public static AjaxResult success(String msg) {
		AjaxResult result = new AjaxResult();
		result.setSuccess(true);
		result.setMsg(msg);
		result.setErrCode(200);
		return result;
	}
	
	public static AjaxResult err(String msg) {
		AjaxResult result = new AjaxResult();
		result.setSuccess(false);
		result.setMsg(msg);
		result.setErrCode(400);
		return result;
	}

}
