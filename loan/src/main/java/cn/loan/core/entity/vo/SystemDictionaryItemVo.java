package cn.loan.core.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典条目视图
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class SystemDictionaryItemVo {
	private Long id;
	private String itemName;
	private String itemKey;
	private String itemValue;
	private Integer sequence;
	private Long systemDictionaryId;
}
