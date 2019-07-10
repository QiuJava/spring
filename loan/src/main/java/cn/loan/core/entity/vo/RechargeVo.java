package cn.loan.core.entity.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 充值视图
 * 
 * @author qiujian
 *
 */
@Getter
@Setter
public class RechargeVo {
	private Long id;
	private String username;
	private String serialNumber;
	private BigDecimal amount;
	private Date tradeTime;
}
