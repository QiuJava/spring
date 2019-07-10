package cn.loan.core.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 页面结果
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class PageResult {

	private List<?> content;
	private Integer totalPages = 1;
	private Integer page = 1;

	public Integer getTotalPages() {
		return totalPages < 1 ? 1 : totalPages;
	}
}
