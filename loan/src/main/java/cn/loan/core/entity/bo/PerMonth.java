package cn.loan.core.entity.bo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 每月
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
@ToString
public class PerMonth {

	private Integer month;
	private BigDecimal amount;

}
