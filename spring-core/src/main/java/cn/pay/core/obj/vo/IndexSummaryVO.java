package cn.pay.core.obj.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 首页借款统计数据
 * 
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IndexSummaryVO {
	private Integer totalBorrowUser;
	private BigDecimal totalBorrowAmount;
	private BigDecimal totalInterestAmount;
}
