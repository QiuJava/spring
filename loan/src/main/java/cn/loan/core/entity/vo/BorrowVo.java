package cn.loan.core.entity.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 借款视图
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class BorrowVo {

	private Long id;
	private String username;
	private String title;
	private BigDecimal borrowAmount;
	private BigDecimal rate;
	private Integer repaymentMonths;
	private String repaymentMethodDisplay;
	private BigDecimal grossInterest;
}
