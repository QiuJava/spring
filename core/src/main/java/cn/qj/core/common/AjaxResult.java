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

}
