package cn.pay.core.pojo.qo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import cn.pay.core.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础条件查询对象
 * 
 * @author Qiujian
 *
 */
@Getter
@Setter
public class BaseConditionQo extends BaseQo {
	private static final long serialVersionUID = 1L;
	protected int state = -1;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected Date beginDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected Date endDate;

	public Date getEndDate() {
		if (endDate != null) {
			// 当前时间的明天
			return DateUtil.endOfDay(endDate);
		}
		return endDate;
	}
}
