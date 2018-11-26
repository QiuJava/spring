package cn.qj.core.common;

import java.io.Serializable;
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

}
