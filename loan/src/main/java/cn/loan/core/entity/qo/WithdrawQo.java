package cn.loan.core.entity.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 提现查询
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class WithdrawQo extends BaseQo {

	private static final long serialVersionUID = 1L;

	private Integer auditStatus;
}
