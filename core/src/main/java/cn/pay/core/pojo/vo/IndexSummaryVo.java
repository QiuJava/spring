package cn.pay.core.pojo.vo;

import java.io.Serializable;
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
public class IndexSummaryVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer totalBorrowUser;
	private BigDecimal totalBorrowAmount;
	private BigDecimal totalInterestAmount;
}
