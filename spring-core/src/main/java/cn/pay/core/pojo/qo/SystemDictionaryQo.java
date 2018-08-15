package cn.pay.core.pojo.qo;

import cn.pay.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统字典查询对象
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
public class SystemDictionaryQo extends PageConditionQo {
	private static final long serialVersionUID = 1L;
	private String keyword;
	private Long systemDictionaryId = -1L;

	public String getKeyword() {
		return StringUtil.hasLength(keyword) ? keyword : null;
	}
}
