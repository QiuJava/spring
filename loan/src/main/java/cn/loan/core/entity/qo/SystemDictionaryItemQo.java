package cn.loan.core.entity.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典明细查询
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class SystemDictionaryItemQo extends BaseQo {
	private static final long serialVersionUID = 1L;
	private String keyword;
	private Long systemDictionaryId;

}
