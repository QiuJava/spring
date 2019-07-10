package cn.loan.core.entity.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典查询
 * 
 * @author qiujian
 *
 */
@Setter
@Getter
public class SystemDictionaryQo extends BaseQo {
	private static final long serialVersionUID = 1L;
	private Long systemDictionaryId;
	private String dictKey;
	private String dictName;

}
