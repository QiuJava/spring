package cn.qj.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典查询对象
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
public class SystemDictionaryQo extends PageConditionQo {
	private static final long serialVersionUID = 1L;
	private String keyword;

	@Override
	public String toString() {
		return "SystemDictionaryQo [keyword=" + keyword + ", currentPage=" + currentPage + ", pageSize=" + pageSize
				+ "]";
	}

}
