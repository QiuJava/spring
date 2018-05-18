package cn.pay.core.obj.vo;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 封装分页查询结果(当前页结果集数据/分页条信息)
 * 
 * @author Qiujian
 *
 */
@Getter
@AllArgsConstructor
public class PageResult {

	private List<?> content;
	private Integer totalPages;
	private Integer currentPage;
	private Integer size;


	public static PageResult empty(Integer pageSize) {
		return new PageResult(Collections.EMPTY_LIST, 0, 1, pageSize);
	}
}
