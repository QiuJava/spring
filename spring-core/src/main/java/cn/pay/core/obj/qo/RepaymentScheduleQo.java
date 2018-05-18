package cn.pay.core.obj.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 还款计划查询对象
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
public class RepaymentScheduleQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;
	private Long userId = -1L;
}
