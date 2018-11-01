package cn.qj.core.pojo.qo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页条件
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Getter
@Setter
public class PageConditionQo implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Integer currentPage = 1;
	protected Integer pageSize = 5;

	public Integer getCurrentPage() {
		return currentPage;
	}
}
