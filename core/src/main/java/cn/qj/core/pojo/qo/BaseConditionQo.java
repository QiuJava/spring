package cn.qj.core.pojo.qo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import cn.qj.core.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础条件
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Getter
@Setter
public class BaseConditionQo extends PageConditionQo {
	private static final long serialVersionUID = 1L;

	protected Integer state = -1;
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
