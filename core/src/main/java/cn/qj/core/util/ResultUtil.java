package cn.qj.core.util;

import java.util.Collections;

import cn.qj.core.common.BaseResult;
import cn.qj.core.common.PageResult;

/**
 * 返回结果工具
 * 
 * @author Qiujian
 * @date 2018/11/26
 */
public class ResultUtil {
	private ResultUtil() {
	}

	public static BaseResult success(String msg) {
		BaseResult result = new BaseResult();
		result.setSuccess(true);
		result.setMsg(msg);
		return result;
	}

	public static BaseResult err(String msg) {
		BaseResult result = new BaseResult();
		result.setSuccess(false);
		result.setMsg(msg);
		return result;
	}
	
	public static PageResult empty(Integer pageSize) {
		return new PageResult(Collections.EMPTY_LIST, 0, 1, pageSize);
	}

}
