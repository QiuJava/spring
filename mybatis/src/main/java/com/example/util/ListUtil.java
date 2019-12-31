package com.example.util;

import java.util.List;

/**
 * 列表工具
 * 
 * @author Qiu Jian
 *
 */
public class ListUtil {
	private ListUtil() {
	}

	/**
	 * 分页
	 * 
	 * @param <T>
	 * @param list     不能为空
	 * @param pageNum  从1开始
	 * @param pageSize 不能是负数
	 * @return
	 */
	public static <T> List<T> page(List<T> list, int pageNum, int pageSize) {
		// 为null 处理
		if (list == null) {
			return null;
		}

		// 空元素集合处理
		int size = list.size();
		// 错误页码和页数据数处理
		if (pageNum < 1 || pageSize < 0) {
			return list.subList(0, 0);
		}

		int totalNum = pageNum * pageSize;
		int previousPageTotalNum = (pageNum - 1) * pageSize;

		int startSize = previousPageTotalNum <= size ? previousPageTotalNum : size;
		int fromIndex = totalNum <= size ? previousPageTotalNum : startSize;

		int toIndex = totalNum > size ? size : totalNum;
		return list.subList(fromIndex, toIndex);
	}
}
