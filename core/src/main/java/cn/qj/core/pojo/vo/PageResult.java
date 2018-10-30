package cn.qj.core.pojo.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 封装分页查询结果(当前页结果集数据/分页条信息)
 * 
 * @author Qiujian
 *
 */
@Getter
@AllArgsConstructor
@ToString
public class PageResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<?> content;
	private Integer totalPages;
	private Integer currentPage;
	private Integer size;

	public static PageResult empty(Integer pageSize) {
		return new PageResult(Collections.EMPTY_LIST, 0, 1, pageSize);
	}
}
