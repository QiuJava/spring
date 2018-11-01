package cn.qj.core.pojo.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页统计
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexSummaryVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer totalBorrowUser;
	private BigDecimal totalBorrowAmount;
	private BigDecimal totalInterestAmount;
}
