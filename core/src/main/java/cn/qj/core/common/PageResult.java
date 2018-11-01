package cn.qj.core.common;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 分页结果
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Getter
@ToString
@AllArgsConstructor
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
