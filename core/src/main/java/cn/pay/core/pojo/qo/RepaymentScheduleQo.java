package cn.pay.core.pojo.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 还款计划查询对象
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
public class RepaymentScheduleQo extends BaseConditionQo {
	private static final long serialVersionUID = 1L;
	private Long userId = -1L;
}
