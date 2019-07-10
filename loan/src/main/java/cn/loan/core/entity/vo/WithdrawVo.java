package cn.loan.core.entity.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 提现视图
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class WithdrawVo {

	private Long id;
	private String username;
	private String realName;
	private String cardNumber;
	private String bankForkName;
	private BigDecimal amount;
	private String bankName;

}
