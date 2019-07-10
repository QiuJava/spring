package cn.qj.core.util;

import java.util.Collections;

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

	public static PageResult empty(Integer pageSize) {
		return new PageResult(Collections.EMPTY_LIST, 0, 1, pageSize);
	}

}
