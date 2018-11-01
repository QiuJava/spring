package cn.qj.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 还款计划条件o
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Setter
@Getter
public class RepaymentScheduleQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;
	private Long userId = -1L;

	@Override
	public String toString() {
		return "RepaymentScheduleQo [userId=" + userId + ", state=" + state + ", beginDate=" + beginDate + ", endDate="
				+ endDate + ", currentPage=" + currentPage + ", pageSize=" + pageSize + "]";
	}

}
