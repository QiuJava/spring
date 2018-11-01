package cn.qj.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典明细条件
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
public class SystemDictionaryItemQo extends PageConditionQo {
	private static final long serialVersionUID = 1L;
	private String title;
	private Long systemDictionaryId;

	@Override
	public String toString() {
		return "SystemDictionaryItemQo [title=" + title + ", systemDictionaryId=" + systemDictionaryId
				+ ", currentPage=" + currentPage + ", pageSize=" + pageSize + "]";
	}

}
