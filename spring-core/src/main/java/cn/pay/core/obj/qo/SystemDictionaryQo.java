package cn.pay.core.obj.qo;

import cn.pay.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Qiujian
 *
 */
@Setter
@Getter
@ToString
public class SystemDictionaryQo extends BaseQo {
	private static final long serialVersionUID = 1L;
	private String keyword;
	private Long systemDictionaryId = -1L;

	public String getKeyword() {
		return StringUtil.hasLength(keyword) ? keyword : null;
	}
}
