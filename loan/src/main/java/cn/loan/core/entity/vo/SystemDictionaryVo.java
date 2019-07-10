package cn.loan.core.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典视图
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class SystemDictionaryVo {
	private Long id;
	private String dictName;
	private String dictKey;
	private String dictValue;
	private Integer sequence;
}
