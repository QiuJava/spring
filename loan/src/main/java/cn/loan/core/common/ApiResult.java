package cn.loan.core.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 结果
 *
 * @author Qiujian
 *
 */
@Getter
@Setter
public class ApiResult {

	private boolean succeed;
	private Integer statusCode;
	private String msg;
	private Object data;
}
