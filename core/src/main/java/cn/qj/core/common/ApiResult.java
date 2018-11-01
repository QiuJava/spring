package cn.qj.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Api接口返回结果
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult {

	private String msg;
	private Integer status;
	private Object data;

}
