package cn.loan.core.entity.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 还款计划
 * @author qiujian
 *
 */
@Getter
@Setter
public class RepaymentPlanQo extends BaseQo {
	private static final long serialVersionUID = 1L;
	
	private Long borrowerId;
}
